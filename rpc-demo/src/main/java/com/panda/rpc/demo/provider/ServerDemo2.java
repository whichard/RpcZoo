/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.demo.provider.ServerDemo1
 * Author:   wq
 * Date:     2019/7/15 18:46
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.demo.provider;

import com.panda.rpc.demo.api.Ihello;
import com.panda.rpc.register.IregisterCenter;
import com.panda.rpc.register.ZkRegisterCenter;
import com.panda.rpc.server.RpcServer;

import java.util.Arrays;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 18:46
 */
public class ServerDemo2 {

	public static void main(String[] args) throws InterruptedException {
		Ihello helloService = new HelloImpl2();
		IregisterCenter registerCenter = new ZkRegisterCenter("localhost:2181");
		RpcServer rpcServer = new RpcServer(registerCenter, "127.0.0.1", 9999);
		rpcServer.bindService(Arrays.asList(helloService));
		rpcServer.publish();
	}
}
