package com.alotofletters.nbt;

import com.alotofletters.nbt.impl.NBTCompound;
import com.alotofletters.nbt.impl.NBTInvalid;
import com.alotofletters.nbt.impl.NBTList;
import com.alotofletters.nbt.impl.NBTWrapper;
import com.alotofletters.nbt.util.ReaderWriter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Supplier;

import static com.alotofletters.nbt.impl.NBTWrapper.createReaderWriter;

public class NBTFile {

	public static final String INVALID 	= "invalid";
	public static final String WRAPPER 	= "wrapper";
	public static final String COMPOUND = "compound";
	public static final String LIST = "list";

	public static final String INT = "int";
	public static final String BYTE = "byte";
	public static final String BOOLEAN = "boolean";
	public static final String SHORT = "short";
	public static final String STRING = "string";
	public static final String FLOAT = "float";

	public static final Map<String, Supplier<INBT<?>>> suppliers = new Hashtable<>();

	protected final File file;

	public NBTFile(String file) {
		this(new File(file));
	}

	public NBTFile(File file) {
		this.file = file;
	}

	public INBTCompound read() throws IOException {
		FileInputStream stream = new FileInputStream(file);
		DataInputStream dataInputStream = new DataInputStream(stream);
		NBTCompound compound = new NBTCompound();
		compound.readFromStream(dataInputStream);
		return compound;
	}

	public void write(@NotNull INBTCompound compound) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		DataOutputStream dataOutputStream = new DataOutputStream(stream);
		compound.writeToStream(dataOutputStream);
		dataOutputStream.flush();
	}

	public boolean exists() {
		return file.exists();
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public String getPath() {
		return file.getPath();
	}

	public static Supplier<INBT<?>> getTag(String type) {
		if (!suppliers.containsKey(type)) {
			System.out.println("!! WARNING !! Invalid tag type found : " + type);
			return NBTInvalid::new;
		}
		return suppliers.get(type);
	}


}
