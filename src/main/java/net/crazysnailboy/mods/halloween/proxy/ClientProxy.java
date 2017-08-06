package net.crazysnailboy.mods.halloween.proxy;

import net.crazysnailboy.mods.halloween.init.ModEntities;
import net.crazysnailboy.mods.halloween.init.ModItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

	@Override
	public void preInit()
	{
		super.preInit();
		this.registerEntityModels();
	}

	@Override
	public void init()
	{
		super.init();
		this.registerColorHandlers();
	}

	@Override
	public void postInit()
	{
		super.postInit();
	}


	private void registerColorHandlers()
	{
		ModItems.registerColorHandlers();
	}

	private void registerEntityModels()
	{
		ModEntities.registerRenderingHandlers();
	}

}
