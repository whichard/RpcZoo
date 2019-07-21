/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.common.ResponseCode
 * Author:   wq
 * Date:     2019/7/15 18:17
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.common;

/**
 * 响应码
 *
 * @author wq
 * @date 2019/7/15 18:17
 */
public enum ResponseCode {

	/**
	 * 成功响应
	 */
	SUCCESS(20000),

	/**
	 * 处理错误
	 */
	ERROR500(500),

	/**
	 * 404找不到出错
	 */
	ERROR404(404),
	;

	private Integer value;

	ResponseCode(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
