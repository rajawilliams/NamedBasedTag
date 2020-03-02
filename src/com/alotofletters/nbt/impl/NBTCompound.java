package com.alotofletters.nbt.impl;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.INBTCompound;
import com.alotofletters.nbt.NBTFile;
import com.alotofletters.nbt.util.ReaderWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import static com.alotofletters.nbt.NBTFile.BOOLEAN;
import static com.alotofletters.nbt.NBTFile.BYTE;
import static com.alotofletters.nbt.NBTFile.COMPOUND;
import static com.alotofletters.nbt.NBTFile.FLOAT;
import static com.alotofletters.nbt.NBTFile.INT;
import static com.alotofletters.nbt.NBTFile.INVALID;
import static com.alotofletters.nbt.NBTFile.LIST;
import static com.alotofletters.nbt.NBTFile.SHORT;
import static com.alotofletters.nbt.NBTFile.STRING;
import static com.alotofletters.nbt.NBTFile.WRAPPER;
import static com.alotofletters.nbt.NBTFile.suppliers;
import static com.alotofletters.nbt.impl.NBTWrapper.createReaderWriter;

public final class NBTCompound implements INBTCompound {

	private Map<String, INBT<?>> tagMap = new Hashtable<>();

	@Override
	public void setTag(String key, INBT<?> tag) {
		tagMap.put(key, tag);
	}

	@Override
	public INBT<?> getTag(String key) {
		return tagMap.get(key);
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Set<String> keySet = tagMap.keySet();
		stream.writeInt(keySet.size());
		for (String key : keySet) {
			INBT<?> inbt = tagMap.get(key);
			stream.writeUTF(key);
			stream.writeUTF(inbt.getType());
			inbt.writeToStream(stream);
		}
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		for (int index = 0; index < length; index++) {
			String key = stream.readUTF();
			INBT<?> tag = NBTFile.getTag(stream.readUTF()).get();
			tag.readFromStream(stream);
			tagMap.put(key, tag);
		}
	}

	@Override
	public void setValue(Map<String, INBT<?>> value) {
		this.tagMap = value;
	}

	@Override
	public Map<String, INBT<?>> getValue() {
		return this.tagMap;
	}

	@Override
	public String getType() {
		return NBTFile.COMPOUND;
	}

	public void setByte(String key, byte value) {
		NBTWrapper<Byte> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(BYTE);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	public void setInt(String key, int value) {
		NBTWrapper<Integer> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(INT);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	public void setFloat(String key, float value) {
		NBTWrapper<Float> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(FLOAT);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	public void setShort(String key, short value) {
		NBTWrapper<Short> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(SHORT);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	public void setBoolean(String key, boolean value) {
		NBTWrapper<Boolean> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(BOOLEAN);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	public void setString(String key, String value) {
		NBTWrapper<String> nbtByte = new NBTWrapper<>();
		nbtByte.setRegistryName(STRING);
		nbtByte.setValue(value);
		setTag(key, nbtByte);
	}

	static {
		suppliers.put(INVALID, NBTInvalid::new);
		suppliers.put(WRAPPER, NBTWrapper::new);
		suppliers.put(COMPOUND, NBTCompound::new);
		suppliers.put(LIST, NBTList::new);
		createReaderWriter(BYTE, new ReaderWriter<>((stream) -> {
			try {
				return stream.readByte();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (byte) 0;
		}, (dataOutputStream, aByte) -> {
			try {
				dataOutputStream.writeByte(aByte);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, BYTE));
		createReaderWriter(INT, new ReaderWriter<>((stream) -> {
			try {
				return stream.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}, (dataOutputStream, aInt) -> {
			try {
				dataOutputStream.writeInt(aInt);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, INT));
		createReaderWriter(SHORT, new ReaderWriter<>((stream) -> {
			try {
				return stream.readShort();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (short)0;
		}, (dataOutputStream, aShort) -> {
			try {
				dataOutputStream.writeShort(aShort);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, SHORT));
		createReaderWriter(BOOLEAN, new ReaderWriter<>((stream) -> {
			try {
				return stream.readByte() == 1;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}, (dataOutputStream, bool) -> {
			try {
				dataOutputStream.writeByte(bool ? 1 : 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, BOOLEAN));
		createReaderWriter(FLOAT, new ReaderWriter<>((stream) -> {
			try {
				return stream.readFloat();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0f;
		}, (dataOutputStream, aFloat) -> {
			try {
				dataOutputStream.writeFloat(aFloat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, FLOAT));
		createReaderWriter(STRING, new ReaderWriter<>((stream) -> {
			try {
				return stream.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}, (dataOutputStream, utf) -> {
			try {
				dataOutputStream.writeUTF(utf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, STRING));
	}
}
