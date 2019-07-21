/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.demo.provider.HelloImpl1
 * Author:   wq
 * Date:     2019/7/15 18:46
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.demo.provider;

import com.panda.rpc.annotation.RpcService;
import com.panda.rpc.demo.api.Ihello;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 18:46
 */
@RpcService(Ihello.class)
public class HelloImpl1 implements Ihello {

	@Override
	public String sayHello(String name) {
		return "HelloImpl1.sayHello:" + name;
	}
}
