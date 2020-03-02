package com.alotofletters.nbt.impl;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.NBTFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTList implements INBT<List<INBT<?>>>, Iterable<INBT<?>> {

	private final List<INBT<?>> elements = new ArrayList<>();

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(elements.size());
		for (INBT<?> inbt : elements) {
			if (inbt != null) {
				stream.writeUTF(inbt.getType());
				inbt.writeToStream(stream);
			} else {
				System.out.println("Element was null! Skipping...");
				stream.writeUTF(NBTFile.INVALID);
			}
		}
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		for (int index = 0; index < length; index++) {
			INBT<?> tag = NBTFile.getTag(stream.readUTF()).get();
			tag.readFromStream(stream);
			elements.add(tag);
		}
	}

	public void add(INBT<?> inbt) {
		elements.add(inbt);
	}

	@Override
	public void setValue(List<INBT<?>> value) {}

	@Override
	public List<INBT<?>> getValue() {
		return this.elements;
	}

	@Override
	public String getType() {
		return NBTFile.LIST;
	}

	@Override
	public Iterator<INBT<?>> iterator() {
		return elements.iterator();
	}

	public void setValue(INBT<?> inbt) {
		this.elements.add(inbt);
	}

	public void setByte(byte value) {
		NBTWrapper<Byte> nbt = new NBTWrapper<>();
		nbt.setRegistryName(NBTFile.BYTE);
		nbt.setValue(value);
		add(nbt);
	}

	public void setInt(int value) {
		NBTWrapper<Integer> nbt = new NBTWrapper<>();
		nbt.setRegistryName(NBTFile.INT);
		nbt.setValue(value);
		add(nbt);
	}

	public void setFloat(float value) {
		NBTWrapper<Float> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(NBTFile.FLOAT);
		nbtByte.setValue(value);
		add(nbtByte);
	}

	public void setShort(byte value) {
		NBTWrapper<Byte> nbt = new NBTWrapper<>();
		nbt.setRegistryName(NBTFile.SHORT);
		nbt.setValue(value);
		add(nbt);
	}

	public void setBoolean(boolean value) {
		NBTWrapper<Boolean> nbt = new NBTWrapper<>();
		nbt.setRegistryName(NBTFile.BOOLEAN);
		nbt.setValue(value);
		add(nbt);
	}

	public void setString(String value) {
		NBTWrapper<String> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(NBTFile.STRING);
		nbtByte.setValue(value);
		add(nbtByte);
	}
}
