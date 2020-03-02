package com.alotofletters.nbt.impl;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.NBTFile;
import com.alotofletters.nbt.util.ReaderWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class NBTWrapper<T> implements INBT<T> {

	private T value;
	private String registryName;

	public static final Map<String, ReaderWriter<?>> READER_WRITER_MAP = new Hashtable<>();

	@Override
	@SuppressWarnings("unchecked")
	public void writeToStream(@NotNull DataOutputStream stream) throws IOException {
		stream.writeUTF(registryName);
		((ReaderWriter<T>) getReaderWriter(registryName)).getWriter().accept(stream, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readFromStream(@NotNull DataInputStream stream) throws IOException {
		this.registryName = stream.readUTF();
		this.value = ((ReaderWriter<T>) getReaderWriter(registryName)).getReader().apply(stream);
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String getType() {
		return NBTFile.WRAPPER;
	}

	@Override
	public String toString() {
		return String.format("NBTWrapper{value=%s}", getValue());
	}

	public void setRegistryName(String registryName) {
		this.registryName = registryName;
	}

	public static ReaderWriter<?> getReaderWriter(String registryName) {
		if (!READER_WRITER_MAP.containsKey(registryName)) {
			return ReaderWriter.EMPTY;
		}
		return READER_WRITER_MAP.get(registryName);
	}

	public static void createReaderWriter(String registryName, ReaderWriter<?> readerWriter) {
		READER_WRITER_MAP.put(registryName, readerWriter);
	}

	@Nullable
	private static String getKey(ReaderWriter<?> readerWriter) {
		for (String key : READER_WRITER_MAP.keySet()) {
			if (READER_WRITER_MAP.get(key).equals(readerWriter)) {
				return key;
			}
		}
		return null;
	}
}
