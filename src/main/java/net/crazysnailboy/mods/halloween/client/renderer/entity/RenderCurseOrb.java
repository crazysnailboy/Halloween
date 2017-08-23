package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.entity.projectile.EntityCurseOrb;
import net.crazysnailboy.mods.halloween.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderCurseOrb extends RenderSnowball<EntityCurseOrb>
{

	public RenderCurseOrb(RenderManager renderManager)
	{
		super(renderManager, ModItems.CURSE_ORB, Minecraft.getMinecraft().getRenderItem());
	}

}