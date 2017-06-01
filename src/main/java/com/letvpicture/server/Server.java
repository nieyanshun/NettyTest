package com.letvpicture.server;

public interface Server {
	
	void bind(int port);

	void bind(String address, int port);
}
