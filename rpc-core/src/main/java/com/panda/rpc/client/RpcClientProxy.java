/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.consumer.RpcClientProxy
 * Author:   wq
 * Date:     2019/7/15 19:08
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.client;

import com.panda.rpc.discover.IServerDiscover;

import java.lang.reflect.Proxy;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 19:08
 */
public class RpcClientProxy {

	private IServerDiscover serverDiscover;

	public RpcClientProxy(IServerDiscover serverDiscover) {
		this.serverDiscover = serverDiscover;
	}

	/**
	 * 基于服务接口和版本创建代理
	 *
	 * @param interfaceCls 服务接口
	 * @param version      版本
	 * @param <T>          泛型
	 *
	 * @return 实现该节点的代理对象
	 */
	public <T> T clientProxy(Class<T> interfaceCls, String version) {
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[] { interfaceCls },
				new RpcInvocationHandler(serverDiscover, version));
	}

	public <T> T clientProxy(Class<T> interfaceCls) {
		return this.clientProxy(interfaceCls, null);
	}
}
