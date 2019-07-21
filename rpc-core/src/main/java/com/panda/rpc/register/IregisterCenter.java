/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.register.IregisterCenter
 * Author:   wq
 * Date:     2019/7/14 19:05
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.register;

/**
 * 注册中心接口
 *
 * @author wq
 * @date 2019/7/14 19:05
 */
public interface IregisterCenter {

	/**
	 * 基于服务名和服务地址注册一个服务
	 * @param serviceName 服务名称
	 * @param serviceAddress 服务地址
	 * @throws Exception 节点创建失败
	 */
	void register(String serviceName,String serviceAddress) throws Exception;
}
