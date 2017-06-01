package com.letvpicture.server;

import java.util.List;

import com.letvpicture.protocol.BinaryMessageDecoder;
import com.letvpicture.protocol.MessageHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

public class IMServer implements Server {

	private String ADDRESS = "127.0.0.1";

	@Override
	public void bind(int port) {
		try {
			doBind(ADDRESS, port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bind(String address, int port) {
		try {
			doBind(address, port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void doBind(String address, int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new BinaryMessageDecoder()).addLast(new MessageHandler());
						}
					});
			ChannelFuture f = bootStrap.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	interface BinaryProtocol {

		byte[] toBinary(Object msg);

		Object fromBinary(byte[] bytes);
	}

	public static void main(String[] args) {
		new IMServer().bind(9000);
	}
}
