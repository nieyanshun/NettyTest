package com.letvpicture.client;

import com.letvpicture.msg.Message.MessageHeader;
import com.letvpicture.msg.MessagePacket;
import com.letvpicture.protocol.BinaryMessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class IMClient implements Client {

	private static String ADDRESS = "127.0.0.1";

	@Override
	public void connect(int port) {
		try {
			doConnect(ADDRESS, port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect(String address, int port) {
		try {
			doConnect(address, port);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doConnect(String address, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// ch.pipeline().addLast(new IMClientHandler());
							ch.pipeline().addLast(new BinaryMessageEncoder());
						}
					});
			ChannelFuture f = b.connect(address, port).sync();
			for (int i = 0; i < 100; i++) {

				String msg = "你好啊小伙子";
				MessagePacket packet = new MessagePacket();
				byte[] body = msg.getBytes();
				MessageHeader header = new MessageHeader(123456, body.length,
						(short) (System.currentTimeMillis() / 1000));
				packet.setHeader(header);
				packet.setBody(body);
				packet.setReq(true);

				f.channel().writeAndFlush(packet);
			}
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	class IMClientHandler extends ChannelHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			String msg = "你好啊小伙子";
			MessagePacket packet = new MessagePacket();
			byte[] body = msg.getBytes();
			MessageHeader header = new MessageHeader(123456, body.length, (short) (System.currentTimeMillis() / 1000));
			packet.setHeader(header);
			packet.setBody(body);
			packet.setReq(true);
			ctx.writeAndFlush(packet);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "UTF-8");
			System.out.println(body);
			// System.out.println("Now is :" + body + ";the counter is " +
			// ++counter);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}

	public static void main(String[] args) {
		new IMClient().connect(9000);
	}
}
