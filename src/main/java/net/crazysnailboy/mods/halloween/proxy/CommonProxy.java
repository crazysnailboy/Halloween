package net.crazysnailboy.mods.halloween.proxy;

import net.crazysnailboy.mods.halloween.init.ModEntities;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.item.crafting.ModRecipes;

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
		ModEntities.registerEntities();
		ModEntities.addEntitySpawns();
	}

	private void registerLootTables()
	{
		ModLootTables.registerLootTables();
	}

	private void registerNetworkMessages()
	{
//		HalloweenMod.NETWORK.registerMessage(HaunterVisibilityMessage.MessageHandler.class, HaunterVisibilityMessage.class, 0, Side.CLIENT);
	}

}
