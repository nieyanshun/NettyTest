package com.letvpicture.protocol;

import java.util.List;
import com.letvpicture.msg.MessagePacket;
import com.letvpicture.msg.Message.MessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class BinaryMessageDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (!in.isReadable()){
			
			System.out.println("Read method .........un readable");
			return;
		}
		if (in.readableBytes() < 12) {
			System.out.println("Read method .......<12..");
			return;
		}
		MessagePacket msg = new MessagePacket();
		
//		byte[] magicBin = in.readBytes(12).array();

		short magic = in.readShort();
		
		

		if ((msg.magic & msg.requestMask) != magic) {
			throw new RuntimeException("Unknown protocol magic code.");
		}
		short timeStamp = in.readShort();
		int id = in.readInt();
		int bodyLength = in.readInt();

		MessageHeader header = new MessageHeader(id, bodyLength, timeStamp);
		msg.setHeader(header);
		if (in.readableBytes() < bodyLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] body = new byte[bodyLength];
		in.readBytes(body, 0, bodyLength);
		msg.setBody(body);
		out.add(msg);
	}

}
