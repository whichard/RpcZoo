/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.discover.loadbalance.RandomLoadBalance
 * Author:   wq
 * Date:     2019/7/15 19:39
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.discover.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载
 *
 * @author wq
 * @date 2019/7/15 19:39
 */
public class RandomLoadBalance extends AbstractLoadBalance {

	@Override
	protected String doSelect(List<String> serviceAddresses) {
		Random random = new Random();
		return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
	}
}
