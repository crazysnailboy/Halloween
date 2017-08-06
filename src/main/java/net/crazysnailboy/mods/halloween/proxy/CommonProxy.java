package net.crazysnailboy.mods.halloween.proxy;

import net.crazysnailboy.mods.halloween.init.ModEntities;
import net.crazysnailboy.mods.halloween.init.ModLootTables;

public class CommonProxy
{

	public void preInit()
	{
		this.registerEntities();
		this.registerLootTables();
	}

	public void init()
	{
	}

	public void postInit()
	{
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

}
