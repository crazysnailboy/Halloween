package net.crazysnailboy.mods.halloween.proxy;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.common.network.ConfigSyncMessage;
import net.crazysnailboy.mods.halloween.common.network.JumpkinRotationMessage;
import net.crazysnailboy.mods.halloween.init.ModEntities;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.item.crafting.ModRecipes;
import net.crazysnailboy.mods.halloween.network.datasync.ModDataSerializers;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{

	public void preInit()
	{
		this.registerEntities();
		this.registerLootTables();
		this.registerNetworkMessages();
	}

	public void init()
	{
		this.registerCraftingRecipes();
	}

	public void postInit()
	{
	}


	private void registerCraftingRecipes()
	{
		ModRecipes.registerCraftingRecipes();
	}

	private void registerEntities()
	{
		ModDataSerializers.registerDataSerializers();
		ModEntities.registerEntities();
		ModEntities.addEntitySpawns();
	}

	private void registerLootTables()
	{
		ModLootTables.registerLootTables();
	}

	private void registerNetworkMessages()
	{
		HalloweenMod.NETWORK.registerMessage(JumpkinRotationMessage.class, JumpkinRotationMessage.class, 0, Side.CLIENT);
		HalloweenMod.NETWORK.registerMessage(ConfigSyncMessage.class, ConfigSyncMessage.class, 1, Side.CLIENT);
	}

}