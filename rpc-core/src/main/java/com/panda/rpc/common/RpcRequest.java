/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.common.RpcRequest
 * Author:   wq
 * Date:     2019/7/14 18:32
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求实体类
 *
 * @author wq
 * @date 2019/7/14 18:32
 */
@Data
public class RpcRequest implements Serializable {

	private static final long serialVersionUID = 5661720043123218215L;

	/**
	 * 请求接口名
	 */
	private String className;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 参数数组
	 */
	private Object[] params;

	/**
	 * 版本号
	 */
	private String version;
}
