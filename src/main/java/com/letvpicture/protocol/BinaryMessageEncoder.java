package com.letvpicture.protocol;

import com.letvpicture.msg.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class BinaryMessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		System.out.println("==============encode");
		out.writeBytes(msg.serialize());
	}

}
