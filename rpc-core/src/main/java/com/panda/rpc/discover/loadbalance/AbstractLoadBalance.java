/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.discover.loadbalance.AbstractLoadBalance
 * Author:   wq
 * Date:     2019/7/15 19:37
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.discover.loadbalance;

import java.util.List;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 19:37
 */
public abstract class AbstractLoadBalance implements ILoadBalance {

	@Override
	public String selectServiceAddress(List<String> serviceAddresses) {
		if (serviceAddresses == null || serviceAddresses.size() == 0) {
			return null;
		}
		if (serviceAddresses.size() == 1) {
			return serviceAddresses.get(0);
		}
		return doSelect(serviceAddresses);
	}

	protected abstract String doSelect(List<String> serviceAddresses);
}
