/*
 * *****************************************************
 * *****************************************************
 * Copyright (C), 2018-2020, panda-fa.com
 * FileName: com.panda.rpc.consumer.NettyTransport
 * Author:   wq
 * Date:     2019/7/16 21:50
 * *****************************************************
 * *****************************************************
 */
package com.panda.rpc.client;

import com.alibaba.fastjson.JSON;
import com.panda.rpc.common.RpcRequest;
import com.panda.rpc.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <Description>
 *
 * @author wq
 * @date 2019/7/16 21:50
 */
@Slf4j
public class NettyTransport {

	private static Bootstrap bootstrap;

	private String host;

	private int port;

	public NettyTransport(String host, int port) {
		this.host = host;
		this.port = port;
	}

	static {
		bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();

		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new LengthFieldPrepender(4));
				pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast(new ClientHandler());
			}
		});
	}

	public RpcResponse send(RpcRequest request) throws InterruptedException {
		ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
		Channel channel = channelFuture.channel();
		channel.writeAndFlush(JSON.toJSONString(request));
		//当通道关闭了，就继续往下走
		channelFuture.channel().closeFuture().sync();
		AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
		return channel.attr(key).get();
	}

	public static class ClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
			log.debug("收到response:{}", s);
			RpcResponse response = JSON.parseObject(s, RpcResponse.class); //把Netty传输的字符串还原为对象
			AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
			channelHandlerContext.channel().attr(key).set(response);
			channelHandlerContext.channel().close();

		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			log.error("Unexpected exception from upstream.", cause);
			super.exceptionCaught(ctx, cause);
		}
	}

}
