/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.consumer.RpcInvocationHandler
 * Author:   wq
 * Date:     2019/7/15 19:15
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.rpc.common.ResponseCode;
import com.panda.rpc.common.RpcRequest;
import com.panda.rpc.common.RpcResponse;
import com.panda.rpc.discover.IServerDiscover;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 19:15
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

	private IServerDiscover serverDiscover;

	private String version;

	public RpcInvocationHandler(IServerDiscover serverDiscover, String version) {
		this.serverDiscover = serverDiscover;
		this.version = version;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			RpcRequest request = new RpcRequest();
			request.setClassName(method.getDeclaringClass().getName());
			request.setMethodName(method.getName());
			request.setParams(args);
			request.setVersion(version);
			String serviceName = method.getDeclaringClass().getName();
			if (null != version && !"".equals(version)) {
				serviceName += "-" + version;
			}
			String servicePath = serverDiscover.disvover(serviceName);
			if (null == servicePath) {
				log.error("并未找到服务地址,className:{}", serviceName);
				throw new RuntimeException("未找到服务地址");
			}
			String host = servicePath.split(":")[0];
			int port = Integer.parseInt(servicePath.split(":")[1]);
			RpcResponse response = new NettyTransport(host, port).send(request);
			if (response == null) {
				throw new RuntimeException("调用服务失败,servicePath:" + servicePath);
			}
			if (response.getCode() == null || !response.getCode().equals(ResponseCode.SUCCESS.getValue())) {
				log.error("调用服务失败,servicePath:{},RpcResponse:{}", servicePath,
						JSONObject.toJSONString(JSON.toJSONString(response)));
				throw new RuntimeException(response.getMessage());
			} else {
				return response.getData();
			}
	}
}
