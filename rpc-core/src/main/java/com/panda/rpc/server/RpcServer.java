/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.server.RpcServer
 * Author:   wq
 * Date:     2019/7/14 19:03
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.server;

import com.panda.rpc.annotation.RpcService;
import com.panda.rpc.register.IregisterCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开启一个rpc远程服务
 *
 * @author wq
 * @date 2019/7/14 19:03
 */
@Slf4j
public class RpcServer {

	/**
	 * 注册中心
	 */
	private IregisterCenter registerCenter;

	/**
	 * 服务发布的ip地址
	 * 这边自定义因为 InetAddress.getLocalHost().getHostAddress()可能获得是127.0.0.1
	 */
	private String serviceIp;

	/**
	 * 服务发布端口
	 */
	private int servicePort;

	/**
	 * 服务名称和服务对象的关系
	 */
	private Map<String, Object> handlerMap = new HashMap<>();

	public RpcServer(IregisterCenter iregisterCenter, String ip, int servicePort) {
		this.registerCenter = iregisterCenter;
		this.serviceIp = ip;
		this.servicePort = servicePort;
	}

	/**
	 * 绑定服务名以及服务对象
	 *
	 * @param services 服务列表
	 */
	public void bindService(List<Object> services) {
		for (Object service : services) {
			RpcService anno = service.getClass().getAnnotation(RpcService.class);
			if (null == anno) {
				//注解为空的情况，version就是空，serviceName就是
				throw new RuntimeException("服务并没有注解，请检查。" + service.getClass().getName());
			}
			String serviceName = anno.value().getName();
			String version = anno.version();
			if (!"".equals(version)) {
				serviceName += "-" + version;
			}
			handlerMap.put(serviceName, service); //注解获取版本号，作为名称的一部分。把传来的服务放进Map
		}
	}

	/**
	 * 发布服务
	 */
	public void publish() throws InterruptedException {
		//使用netty开启一个服务
		ServerBootstrap bootstrap = new ServerBootstrap();
		NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		bootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						//数据分包，组包，粘包
						p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
						p.addLast(new LengthFieldPrepender(4));
						p.addLast(new StringDecoder(CharsetUtil.UTF_8));
						p.addLast(new StringEncoder(CharsetUtil.UTF_8));
						p.addLast(new ProcessRequestHandler(handlerMap));
					}
				});
		bootstrap.bind(serviceIp, servicePort).sync();
		log.info("成功启动服务,host:{},port:{}", serviceIp, servicePort);
		//服务注册
		handlerMap.keySet().forEach(serviceName -> {
			try {
				registerCenter.register(serviceName, serviceIp + ":" + servicePort);
			} catch (Exception e) {

				log.error("服务注册失败,e:{}", e.getMessage());
				throw new RuntimeException("服务注册失败");
			}
			log.info("成功注册服务，服务名称：{},服务地址：{}", serviceName, serviceIp + ":" + servicePort);
		});
	}
}
