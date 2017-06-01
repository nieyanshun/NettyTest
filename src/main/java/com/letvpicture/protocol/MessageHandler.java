package com.letvpicture.protocol;

import com.letvpicture.msg.MessagePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageHandler extends SimpleChannelInboundHandler {

	int count = 0;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessagePacket packet = (MessagePacket) msg;

		System.out.println("=========Packet ===========" + ++count);
		System.out.println(new String(packet.getBody(), "UTF-8"));
	}

}
