package com.alotofletters.nbt.net;

import com.alotofletters.nbt.impl.NBTCompound;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Map;

public class NBTServer {

	private final ServerSocket serverSocket;
	private int readTimeout = Integer.MAX_VALUE;

	Map<InetAddress, NBTServerWorker> workers = new Hashtable<>();

	public NBTServer(int port) throws IOException {
		this(new ServerSocket(port));
	}

	public NBTServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public NBTServerWorker listen() throws IOException {
		NBTServerWorker worker = new NBTServerWorker(this, new NBTSocket(serverSocket.accept()));
		workers.put(worker.getAddress(), worker);
		return worker;
	}

	public void setTimeout(int timeOut) throws SocketException {
		this.serverSocket.setSoTimeout(timeOut);
	}

	public void remove(InetAddress address) {
		workers.remove(address);
	}

	public void broadcast(NBTCompound compound) throws IOException {
		for (InetAddress address : workers.keySet()) {
			NBTServerWorker worker = workers.get(address);
			worker.write(compound);
		}
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void close() throws IOException {
		this.serverSocket.close();
	}
}
