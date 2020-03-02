package com.alotofletters.nbt.test.net;

import com.alotofletters.nbt.impl.NBTCompound;
import com.alotofletters.nbt.net.NBTServer;
import com.alotofletters.nbt.net.NBTServerWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainServer {

	public static void main(String[] args) throws IOException {
		NBTServer server = new NBTServer(27015);
		BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
		Map<InetAddress, Runnable> runnableMap = new ConcurrentHashMap<>();
		Map<InetAddress, NBTServerWorker> workerList = new ConcurrentHashMap<>();
		Map<InetAddress, Thread> threadMap = new ConcurrentHashMap<>();
		Map<InetAddress, String> nicknames = new ConcurrentHashMap<>();
		AtomicBoolean interrupted = new AtomicBoolean(false);
		Runnable listenForNewClients = () -> {
			try {
				NBTServerWorker worker = server.listen();
				InetAddress address = worker.getAddress();
				System.out.printf("%s has connected!%n", address.getCanonicalHostName());
				broadcast(server, String.format("%s has connected!", address.getCanonicalHostName()));
				System.out.println(threadMap.get(address));
				nicknames.put(address, address.getCanonicalHostName());
				workerList.put(address, worker);
				runnableMap.put(address, () -> {
					if (worker.isClosed()) {
						forceDisconnect(server, runnableMap, workerList, threadMap, nicknames.get(address) + " disconnected", address);
					}
					try {
						NBTCompound compound = worker.read();
						if (compound != null) {
							String packetRecieved = String.valueOf(compound.getTag("Packet").getValue());
							if (packetRecieved.startsWith("/nick")) {
								String addressName = address.getCanonicalHostName();
								String nickname = nicknames.get(address);
								String newNickname = packetRecieved.substring(6).trim();
								System.out.printf(
										"%s changed nick to %s (%s -> %s)%n", addressName, newNickname, nickname, newNickname);
								nicknames.put(address, newNickname);
							} else {
								System.out.printf(
										"<%s> %s%n", nicknames.get(address), packetRecieved);
								broadcast(server, String.format("<%s> %s", nicknames.get(address), packetRecieved));
							}
						} else {
							forceDisconnect(server, runnableMap, workerList, threadMap, nicknames.get(address) + " disconnected", address);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				Thread thread = new Thread(runnableMap.get(address), "Worker-" + address.getCanonicalHostName());
				thread.start();
				threadMap.put(address, thread);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		Runnable listenForStop = () -> {
			String line = null;
			try {
				line = scanner.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line != null && line.equalsIgnoreCase("stop")) {
				interrupted.set(true);
				workerList.forEach(((address, nbtServerWorker) -> {
					try {
						nbtServerWorker.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
				System.out.println("Server stopped!");
				System.gc();
				System.exit(0);
			}
		};
		Thread listenThread = null;
		Thread stopThread = null;
		do {
			if (listenThread == null || !listenThread.isAlive()) {
				listenThread = new Thread(listenForNewClients, "Listen-Server");
				listenThread.start();
			}
			if (stopThread == null || !stopThread.isAlive()) {
				stopThread = new Thread(listenForStop, "Stop-Server");
				stopThread.start();
			}
			workerList.forEach(((address, nbtServerWorker) -> {
				Thread thread = threadMap.get(address);
				Runnable runnable = runnableMap.get(address);
				if (thread == null || !thread.isAlive()) {
					thread = new Thread(runnable, "Worker-" + address.getCanonicalHostName());
					thread.start();
				}
				threadMap.put(address, thread);
			}));
		} while (!interrupted.get());
	}

	private static void forceDisconnect(NBTServer server, Map<InetAddress, Runnable> runnableMap, Map<InetAddress, NBTServerWorker> workerList, Map<InetAddress, Thread> threadMap, String x, InetAddress address) {
		System.out.println(x);
		try {
			broadcast(server, x);
		} catch (IOException e) {
			e.printStackTrace();
		}
		workerList.remove(address);
		runnableMap.remove(address);
		threadMap.get(address).interrupt();
		threadMap.remove(address);
	}

	private static void broadcast(NBTServer server, String string) throws IOException {
		NBTCompound compound = new NBTCompound();
		compound.setString("Packet", string);
		server.broadcast(compound);
	}
}
