package com.alotofletters.nbt;

import java.util.Map;

public interface INBTCompound extends INBT<Map<String, INBT<?>>> {

	void setTag(String key, INBT<?> tag);
	INBT<?> getTag(String key);
}
