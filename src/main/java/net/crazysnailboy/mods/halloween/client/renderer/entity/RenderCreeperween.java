package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.entity.monster.EntityCreeperween;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCreeperween extends RenderLiving<EntityCreeperween>
{

	private static final ResourceLocation CREEPERWEEN_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/creeperween/creeperween.png");


	public RenderCreeperween(RenderManager renderManager)
	{
		super(renderManager, new ModelCreeper(), 0.4F);
	}


	@Override
	protected void preRenderCallback(EntityCreeperween entity, float partialTickTime)
	{
		float f = entity.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float width = (1.625F + f * 0.4F) * f1;
		float height = (1.625F + f * 0.1F) / f1;
		GlStateManager.scale(width, height, width);
	}

	@Override
	protected int getColorMultiplier(EntityCreeperween entity, float lightBrightness, float partialTickTime)
	{
		float f = entity.getCreeperFlashIntensity(partialTickTime);

		if ((int)(f * 10.0F) % 2 == 0)
		{
			return 0;
		}
		else
		{
			int i = (int)(f * 0.2F * 255.0F);
			i = MathHelper.clamp(i, 0, 255);
			return i << 24 | 0x30FFFFFF; // 822083583;
		}
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityCreeperween entity)
	{
		return CREEPERWEEN_TEXTURES;
	}

}
