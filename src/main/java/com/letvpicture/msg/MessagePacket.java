package com.letvpicture.msg;


public class MessagePacket implements Message {

	private static final long serialVersionUID = 1L;

	private MessageHeader header;

	private byte[] body;

	private boolean isReq;

	/**
	 * @return the header
	 */
	public MessageHeader getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(MessageHeader header) {
		this.header = header;
	}

	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * @param isReq
	 *            the isReq to set
	 */
	public void setReq(boolean isReq) {
		this.isReq = isReq;
	}

	@Override
	public byte[] serialize() {

		final int length = header.getBodyLength() + 12;

		final byte[] totalPacket = new byte[length];

		totalPacket[0] = magic >> 8;

		if (isReq()) {
			totalPacket[1] = (byte) (magic & requestMask & 0x00FF);
		} else {
			totalPacket[1] = (byte) (magic & responseMask & 0x00FF);
		}
		final short timeStamp = getHeader().getTimeStamp();

		totalPacket[2] = (byte) (timeStamp >> 8);
		totalPacket[3] = (byte) timeStamp;
		final int id = getHeader().getId();
		totalPacket[4] = (byte) (id >> 24);
		totalPacket[5] = (byte) (id >> 16);
		totalPacket[6] = (byte) (id >> 8);
		totalPacket[7] = (byte) (id);
		totalPacket[8] = (byte) (header.getBodyLength() >> 24);
		totalPacket[8] = (byte) (header.getBodyLength() >> 16);
		totalPacket[10] = (byte) (header.getBodyLength() >> 8);
		totalPacket[11] = (byte) (header.getBodyLength());

		System.arraycopy(getBody(), 0, totalPacket, 12, header.getBodyLength());
		System.out.println("totalPacket length : " + totalPacket.length);
		return totalPacket;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis() / 1000);
		short timeStamp = (short) (System.currentTimeMillis() / 1000);
		System.out.println(timeStamp);
	}

	@Override
	public boolean isReq() {
		return (this.isReq);
	}
}
