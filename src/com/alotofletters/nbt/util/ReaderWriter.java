package com.alotofletters.nbt.util;

import com.alotofletters.nbt.NBTFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ReaderWriter<T> {

	public static final ReaderWriter<String> EMPTY = new ReaderWriter<>((stream) -> null, (stream, str) -> {}, NBTFile.INVALID);

	private final Function<DataInputStream, T> reader;
	private final BiConsumer<DataOutputStream, T> writer;

	private final String type;

	public ReaderWriter(Function<DataInputStream, T> reader, BiConsumer<DataOutputStream, T> writer, String type) {
		this.reader = reader;
		this.writer = writer;
		this.type = type;
	}

	public Function<DataInputStream, T> getReader() {
		return reader;
	}

	public BiConsumer<DataOutputStream, T> getWriter() {
		return writer;
	}

	@Override
	public String toString() {
		return "ReaderWriter{" +
				"type='" + type + '\'' +
				'}';
	}
}
