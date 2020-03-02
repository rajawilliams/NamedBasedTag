package com.alotofletters.nbt.test;

import com.alotofletters.nbt.INBT;
import com.alotofletters.nbt.INBTCompound;
import com.alotofletters.nbt.NBTFile;
import com.alotofletters.nbt.impl.NBTCompound;
import com.alotofletters.nbt.impl.NBTList;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

	public static final File runDir = new File("run");

	public static void main(String[] args) throws IOException {
		if (runDir.mkdirs()) {
			System.out.println("Created run dir!");
		}
		NBTFile file = new NBTFile("testFile.dat");
		if (file.exists()) {
			INBTCompound compound = file.read();
			INBT.printNBT(compound, "Root", 0);
		} else {
			NBTCompound compound = new NBTCompound();
			Random random = new Random();
			NBTList list = new NBTList();
			for (int i = 0; i < 6; i++) {
				Player player = new Player(random.nextInt(50),
						new ItemStack("string", random.nextInt(6) + 1, 0));
				player.position = Vector3f.randomVector(random, 64);
				NBTCompound playerCompound = new NBTCompound();
				player.writeToNBT(playerCompound);
				list.setValue(playerCompound);
			}
			compound.setTag("Players", list);
			file.write(compound);
		}
	}



	static class Player {

		private final int textureId;
		private ItemStack currentItemStack;
		private Vector3f position = new Vector3f();

		public Player(int textureId, ItemStack currentItemStack) {
			this.textureId = textureId;
			this.currentItemStack = currentItemStack;
		}

		public void writeToNBT(NBTCompound compound) {
			compound.setInt("TextureId", textureId);
			NBTCompound itemStack = new NBTCompound();
			currentItemStack.writeToNBT(itemStack);
			compound.setTag("CurrentItem", itemStack);
			NBTCompound vector = new NBTCompound();
			position.writeToNBT(vector);
			compound.setTag("Position", vector);
		}
	}

	static class ItemStack {

		private final String item;
		private int itemAmount;
		private int itemDamage;

		public ItemStack(String item, int itemAmount, int itemDamage) {
			this.item = item;
			this.itemAmount = itemAmount;
			this.itemDamage = itemDamage;
		}

		public void writeToNBT(NBTCompound compound) {
			compound.setString("Id", item);
			compound.setInt("Amount", itemAmount);
			compound.setInt("Damage", itemDamage);
		}
	}

	static class Vector3f {

		public float x = 0;
		public float y = 0;
		public float z = 0;

		public void writeToNBT(NBTCompound compound) {
			compound.setFloat("x", x);
			compound.setFloat("y", y);
			compound.setFloat("z", z);
		}

		public static Vector3f randomVector(Random random, float bound) {
			Vector3f vector3f = new Vector3f();
			vector3f.x = (random.nextFloat() * bound * 2) - bound;
			vector3f.y = (random.nextFloat() * bound * 2) - bound;
			vector3f.z = (random.nextFloat() * bound * 2) - bound;
			return vector3f;
		}
	}
}
