package com.panda.rpc.annotation;/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.annotation.RpcService
 * Author:   wq
 * Date:     2019/7/14 18:55
 * *****************************************************
 * *****************************************************
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提供服务的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

	/**
	 * 对外发布的服务接口地址
	 */
	Class<?> value();

	/**
	 * 版本
	 */
	String version() default "";

}
