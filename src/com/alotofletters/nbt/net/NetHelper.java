package com.alotofletters.nbt.net;

import com.alotofletters.nbt.impl.NBTCompound;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetHelper {

	public static final char START_CHAR = '\u0001';

	public static NBTCompound readFromStream(DataInputStream readStream, int readTimeout) throws IOException {
		NBTCompound compound = null;
		int attempts = 0;
		while (++attempts < readTimeout) {
			byte aByte = readStream.readByte();
			if (aByte == START_CHAR) {
				compound = new NBTCompound();
				compound.readFromStream(readStream);
				break;
			}
		}
		return compound;
	}

	public static void writeToStream(@NotNull NBTCompound compound, @NotNull DataOutputStream writeStream) throws IOException {
		writeStream.writeByte(START_CHAR);
		compound.writeToStream(writeStream);
	}
}
