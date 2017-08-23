package net.crazysnailboy.mods.halloween.init;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;


public class ModLootTables
{

	public static final ResourceLocation ENTITIES_HALLOWMOB = new ResourceLocation(HalloweenMod.MODID, "entities/hallowmob");
	public static final ResourceLocation ENTITIES_HALLOWITCH = new ResourceLocation(HalloweenMod.MODID, "entities/hallowitch");
	public static final ResourceLocation ENTITIES_HAUNTER = new ResourceLocation(HalloweenMod.MODID, "entities/haunter");


	public static void registerLootTables()
	{
		LootTableList.register(ENTITIES_HALLOWMOB);
		LootTableList.register(ENTITIES_HALLOWITCH);
		LootTableList.register(ENTITIES_HAUNTER);
	}

}