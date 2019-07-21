/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.discover.ZkServerDiscover
 * Author:   wq
 * Date:     2019/7/15 19:25
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.discover;

import com.panda.rpc.discover.loadbalance.ILoadBalance;
import com.panda.rpc.discover.loadbalance.RandomLoadBalance;
import com.panda.rpc.register.ZkRegisterCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 19:25
 */
@Slf4j
public class ZkServerDiscover implements IServerDiscover {

	Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();

	private String connectionAddress;

	private ILoadBalance iLoadBalance;

	private CuratorFramework curatorFramework;

	/**
	 * 该线程负责定时更新服务列表
	 */
//	private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	public ZkServerDiscover(String connectionAddress) {
		this.connectionAddress = connectionAddress;
		//默认随机负载
		this.iLoadBalance = new RandomLoadBalance();
		curatorFramework = CuratorFrameworkFactory.builder().
				connectString(connectionAddress).
				sessionTimeoutMs(15000).
				retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
		curatorFramework.start();
//		//更新一次服务列表
//		try {
//			this.updateServiceAddressMap();
//		} catch (Exception e) {
//			log.error("更新服务列表出现异常，e:{}", e.getMessage());
//			throw new RuntimeException("更新服务列表出现异常");
//		}
//		//定时更新,30s更新一次
//		executorService.scheduleAtFixedRate(() -> {
//			try {
//				updateServiceAddressMap();
//			} catch (Exception e) {
//				log.error("更新服务列表出现异常，e:{}", e.getMessage());
//				//这边吃掉异常，可能是zk服务问题
//			}
//		}, 5, 30, TimeUnit.SECONDS);
	}

	/**
	 * 更新服务列表
	 */
//	private void updateServiceAddressMap() throws Exception {
//		List<String> serviceNames = curatorFramework.getChildren().forPath(ZkRegisterCenter.ZK_REGISTER_PATH);
//		//清除没有serviceName的值
//		Set<String> serviceNameSet = new HashSet<>(serviceNames);
//		serviceAddressMap.keySet().forEach(key -> {
//			if (!serviceNameSet.contains(key)) {
//				//不存在的服务本地给清除
//				serviceAddressMap.remove(key);
//			}
//		});
//		//更新所有serviceName的列表
//		for (String serviceName : serviceNames) {
//			String path = ZkRegisterCenter.ZK_REGISTER_PATH + "/" + serviceName;
//			List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
//			serviceAddressMap.put(serviceName, serviceAddresses);
//			//添加订阅
//			registerWatcher(serviceName);
//		}
//	}

	/**
	 * 可以手动设置负载算法
	 *
	 * @param iLoadBalance 负载算法
	 */
	public void setiLoadBalance(ILoadBalance iLoadBalance) {
		this.iLoadBalance = iLoadBalance;
	}

	@Override
	public String disvover(String serviceName) {
		List<String> serviceAddresses;
		if (!serviceAddressMap.containsKey(serviceName)) {
			//
			String path = ZkRegisterCenter.ZK_REGISTER_PATH + "/" + serviceName;
			try {
				serviceAddresses = curatorFramework.getChildren().forPath(path);
				serviceAddressMap.put(serviceName, serviceAddresses);
				registerWatcher(serviceName);
			} catch (Exception e) {
				if (e instanceof KeeperException.NoNodeException) {
					log.error("未获得该节点,serviceName:{}", serviceName);
					serviceAddresses = null;
				} else {
					throw new RuntimeException("获取子节点异常：" + e);
				}
			}
		} else {
			serviceAddresses = serviceAddressMap.get(serviceName);
		}
		return iLoadBalance.selectServiceAddress(serviceAddresses);
	}

	/**
	 * 注册监听
	 *
	 * @param serviceName 服务名称
	 */
	private void registerWatcher(String serviceName) {
		String path = ZkRegisterCenter.ZK_REGISTER_PATH + "/" + serviceName;
		PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
		PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
			List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
			serviceAddressMap.put(serviceName, serviceAddresses);
		};
		childrenCache.getListenable().addListener(pathChildrenCacheListener);
		try {
			childrenCache.start();
		} catch (Exception e) {
			throw new RuntimeException("注册PatchChild Watcher 异常" + e);
		}
	}
}
