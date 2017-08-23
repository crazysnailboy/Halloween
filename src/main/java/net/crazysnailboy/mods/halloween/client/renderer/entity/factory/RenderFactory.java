package net.crazysnailboy.mods.halloween.client.renderer.entity.factory;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderFactory<T extends Entity> implements IRenderFactory<T>
{

	private final Class<? extends Render<? super T>> renderClass;


	public RenderFactory(Class<? extends Render<? super T>> renderClass)
	{
		this.renderClass = renderClass;
	}


	@Override
	public Render<? super T> createRenderFor(RenderManager renderManager)
	{
		try
		{
			return renderClass.getConstructor(RenderManager.class).newInstance(renderManager);
		}
		catch (Exception ex)
		{
			HalloweenMod.LOGGER.catching(ex);
			return null;
		}
	}


	public static final <T extends Entity> RenderFactory<T> create(Class<? extends Render<? super T>> renderClass)
	{
		return new RenderFactory<T>(renderClass);
	}

}