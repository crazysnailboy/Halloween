package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderFakeArrow extends RenderArrow<EntityFakeArrow>
{

	public static final ResourceLocation ARROW_TEXTURES = new ResourceLocation("textures/entity/projectiles/arrow.png");

	public RenderFakeArrow(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFakeArrow entity)
	{
		return ARROW_TEXTURES;
	}

}
