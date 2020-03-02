package com.alotofletters.nbt.net;

import com.alotofletters.nbt.impl.NBTCompound;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

public class NBTServerWorker {

	private NBTServer server;
	private final NBTSocket currentClient;

	private boolean closed = false;

	public NBTServerWorker(NBTServer server, NBTSocket currentClient) {
		this.server = server;
		this.currentClient = currentClient;
	}

	public NBTCompound read() throws IOException {
		try {
			int readTimeout = Integer.MAX_VALUE;
			return NetHelper.readFromStream(currentClient.getInputStream(), readTimeout);
		} catch(SocketException e) {
			this.currentClient.close();
		}
		return null;
	}

	public void write(NBTCompound compound) throws IOException {
		NetHelper.writeToStream(compound, currentClient.getOutputStream());
	}

	public InetAddress getAddress() {
		return currentClient.getAddress();
	}

	public void close() throws IOException {
		this.closed = true;
		server.workers.remove(this.getAddress());
		currentClient.close();
	}

	public boolean isClosed() {
		return closed;
	}
}
