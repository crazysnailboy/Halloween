package net.crazysnailboy.mods.halloween.item;

import net.minecraft.util.ResourceLocation;

public interface ISubItem
{
	ResourceLocation getRegistryName();
	String getUnlocalizedName();
	int getMetadata();
}
