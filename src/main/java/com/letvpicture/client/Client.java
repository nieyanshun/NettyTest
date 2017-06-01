package com.letvpicture.client;

public interface Client {
	void connect(int port);

	void connect(String address, int port);
}
