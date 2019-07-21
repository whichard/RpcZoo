/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.register.ZkRegisterCenter
 * Author:   wq
 * Date:     2019/7/15 18:31
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 18:31
 */
@Slf4j
public class ZkRegisterCenter implements IregisterCenter {

	public static final String ZK_REGISTER_PATH = "/rpc";

	private String connectionAddress;

	private CuratorFramework curatorFramework;

	public ZkRegisterCenter(String connectionAddress) {
		this.connectionAddress = connectionAddress;
		//初始化curator
		curatorFramework = CuratorFrameworkFactory.builder().connectString(connectionAddress).sessionTimeoutMs(15000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
		curatorFramework.start();
	}

	@Override
	public void register(String serviceName, String serviceAddress) throws Exception {
		//需要注册的服务根节点
		String servicePath = ZK_REGISTER_PATH + "/" + serviceName;
		//注册服务，创建临时节点
		String serviceAddr = servicePath + "/" + serviceAddress;
		String nodePath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
				.forPath(serviceAddr, "".getBytes());
		log.debug("节点创建成功，节点为:{}", nodePath);
	}
}
