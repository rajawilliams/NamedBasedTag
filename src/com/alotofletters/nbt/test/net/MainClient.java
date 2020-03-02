package com.alotofletters.nbt.test.net;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.INBTCompound;
import com.alotofletters.nbt.impl.NBTCompound;
import com.alotofletters.nbt.net.NBTSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MainClient {

	public static void main(String[] args) throws IOException {
		NBTSocket socket = new NBTSocket("24.17.91.152", 27015);
		BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
		AtomicReference<String> line = new AtomicReference<>("");
		AtomicBoolean interrupted = new AtomicBoolean(false);
		Runnable runnable = () -> {
			try {
				line.set(scanner.readLine());
				NBTCompound packet = new NBTCompound();
				packet.setString("Packet", line.get());
				socket.write(packet);
			} catch (IOException e) {
				System.out.println("Lost connection! Cause: " + e.getLocalizedMessage());
				interrupted.set(true);
			}
		};
		Runnable runnable2 = () -> {
			INBTCompound compound = null;
			try {
				compound = socket.read();
			} catch (IOException e) {
				interrupted.set(true);
			}
			if (compound != null) {
				String packetRecieved = String.valueOf(compound.getTag("Packet").getValue());
				System.out.println(packetRecieved);
			}
		};
		Thread thread1 = null;
		Thread thread2 = null;
		do {
			if (thread1 == null || !thread1.isAlive()) {
				thread1 = new Thread(runnable, "Receiving-Client");
				thread1.start();
			}
			if (thread2 == null || !thread2.isAlive()) {
				thread2 = new Thread(runnable2, "Sending-Client");
				thread2.start();
			}
		} while (!interrupted.get());
		thread1.interrupt();
		thread2.interrupt();
		socket.close();
		scanner.close();
	}
}
