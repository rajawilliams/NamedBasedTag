package com.alotofletters.nbt.impl;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.NBTFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTInvalid implements INBT<String> {
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
	}

	@Override
	public void setValue(String value) {
	}

	@Override
	public String getValue() {
		return "INVALID";
	}

	@Override
	public String getType() {
		return NBTFile.INVALID;
	}

}
