/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.discover.loadbalance.ILoadBalance
 * Author:   wq
 * Date:     2019/7/15 19:27
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.discover.loadbalance;

import java.util.List;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/15 19:27
 */
public interface ILoadBalance {

	/**
	 * 在已有服务列表中选择一个服务路径
	 * @param serviceAddresses 服务地址列表
	 *
	 * @return 服务地址
	 */
	String selectServiceAddress(List<String> serviceAddresses);

}
