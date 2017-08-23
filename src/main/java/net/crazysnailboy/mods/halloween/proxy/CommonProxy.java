package net.crazysnailboy.mods.halloween.proxy;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.common.network.JumpkinRotationMessage;
import net.crazysnailboy.mods.halloween.init.ModEntities;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
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

	private void registerNetworkMessages()
	{
		HalloweenMod.NETWORK.registerMessage(JumpkinRotationMessage.class, JumpkinRotationMessage.class, 0, Side.CLIENT);
	}

}