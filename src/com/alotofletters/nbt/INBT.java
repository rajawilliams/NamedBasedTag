package com.alotofletters.nbt;

import com.alotofletters.nbt.impl.NBTCompound;
import com.alotofletters.nbt.impl.NBTList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface INBT<T> {

	void writeToStream(DataOutputStream stream) throws IOException;
	void readFromStream(DataInputStream stream) throws IOException;

	void setValue(T value);
	T getValue();

	String getType();

	static void printNBT(INBT<?> inbt) {
		printNBT(inbt, "Root", 0);
	}

	static void printNBT(INBT<?> inbt, String nbtKey, int depth) {
		switch (inbt.getType()) {
			case NBTFile.LIST:
				System.out.println(bracket(depth) + nbtKey + " : List");
				List<INBT<?>> inbtList = ((NBTList) inbt).getValue();
				for (int i = 0; i < inbtList.size(); i++) {
					printNBT(inbtList.get(i), String.valueOf(i), depth + 1);
				}
				break;
			case NBTFile.COMPOUND:
				if (inbt instanceof NBTCompound) {
					System.out.println(bracket(depth) + nbtKey + " : Compound");
					Map<String, INBT<?>> map = ((NBTCompound) inbt).getValue();
					for (String key : map.keySet())
						printNBT(map.get(key), key,depth + 1);
				}
				break;
			default:
				System.out.println(bracket(depth) + nbtKey + " : " + inbt.getValue());
				break;
		}
	}

	static String bracket(int depth) {
		StringBuilder out = new StringBuilder("[" + depth + "]");
		for (int i = 0; i < depth; i++) {
			out.append("\t");
		}
		out.append(" ");
		return out.toString();
	}
}
