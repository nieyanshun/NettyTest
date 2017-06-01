package com.letvpicture.msg;

import java.io.Serializable;

/**
 * @author nieyanshun
 *
 */
public interface Message extends Serializable {
	final static short magic = 0x0666;

	final static short responseMask = 0x0FF2;

	final static short requestMask = 0x0FF4;

	final static short requestCode = 0x0664;

	final static short responseCode = 0x0662;

	boolean isReq();

	MessageHeader getHeader();

	byte[] getBody();

	byte[] serialize();

	public class MessageHeader {
		short magic;
		short timeStamp;
		int id;
		int bodyLength;

		public MessageHeader(int id, int bodyLength, short timeStamp) {
			magic = MessagePacket.magic;
			this.bodyLength = bodyLength;
			this.timeStamp = timeStamp;
			this.id = id;
		}

		/**
		 * @return the magic
		 */
		public short getMagic() {
			return magic;
		}

		/**
		 * @return the timeStamp
		 */
		public short getTimeStamp() {
			return timeStamp;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return the bodyLength
		 */
		public int getBodyLength() {
			return bodyLength;
		}

	}

}
