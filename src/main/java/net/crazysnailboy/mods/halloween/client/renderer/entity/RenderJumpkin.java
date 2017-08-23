package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.model.ModelJumpkin;
import net.crazysnailboy.mods.halloween.entity.monster.EntityJumpkin;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderJumpkin extends RenderLiving<EntityJumpkin>
{

	private static final ResourceLocation JUMPKIN_ON_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/jumpkin/jumpkin_on.png");
	private static final ResourceLocation JUMPKIN_OFF_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/jumpkin/jumpkin_off.png");


	public RenderJumpkin(RenderManager renderManager)
	{
		super(renderManager, new ModelJumpkin(), 0.25F);
	}


	@Override
	public void doRender(EntityJumpkin entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.shadowSize = 0.25F * (float)entity.getSlimeSize();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected void preRenderCallback(EntityJumpkin entity, float partialTickTime)
	{
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		float f1 = (float)entity.getSlimeSize();
		float f2 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTickTime) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityJumpkin entity)
	{
		return entity.getLit() ? JUMPKIN_ON_TEXTURES : JUMPKIN_OFF_TEXTURES;
	}

}