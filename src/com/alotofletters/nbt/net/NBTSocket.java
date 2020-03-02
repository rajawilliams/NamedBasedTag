package com.alotofletters.nbt.net;

import com.alotofletters.nbt.INBTCompound;
import com.alotofletters.nbt.impl.NBTCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NBTSocket {

	private final Socket socket;
	private final DataInputStream readStream;
	private final DataOutputStream writeStream;

	private int timeOut = Integer.MAX_VALUE;

	public NBTSocket(String hostName, int port) throws IOException {
		this(new Socket(hostName, port));
	}

	public NBTSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.readStream = new DataInputStream(socket.getInputStream());
		this.writeStream = new DataOutputStream(socket.getOutputStream());
	}

	public INBTCompound read() throws IOException {
		return NetHelper.readFromStream(this.readStream, this.timeOut);
	}

	public void write(NBTCompound compound) throws IOException {
		NetHelper.writeToStream(compound, this.writeStream);
	}

	public void close() throws IOException {
		this.socket.close();
		this.readStream.close();
		this.writeStream.close();
	}

	public DataInputStream getInputStream() {
		return readStream;
	}

	public DataOutputStream getOutputStream() {
		return writeStream;
	}

	public InetAddress getAddress() {
		return socket.getLocalAddress();
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
}
