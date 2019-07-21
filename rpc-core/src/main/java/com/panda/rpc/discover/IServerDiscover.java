/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.discover.IServerDiscover
 * Author:   wq
 * Date:     2019/7/14 19:05
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.discover;

/**
 * 服务发现接口
 *
 * @author wq
 * @date 2019/7/14 19:05
 */
public interface IServerDiscover {

	/**
	 * 基于服务名称获得一个远程地址
	 *
	 * @param serviceName 服务名称
	 *
	 * @return 远程地址
	 */
	String disvover(String serviceName);
}
