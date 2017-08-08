package net.crazysnailboy.mods.halloween.client.renderer.entity.layers;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderCreeperween;
import net.crazysnailboy.mods.halloween.entity.monster.EntityCreeperween;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class LayerCreeperweenEyes<T extends EntityCreeperween> implements LayerRenderer<EntityCreeperween>
{

	private static final ResourceLocation CREEPERWEEN_EYES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/creeperween/creeperween_eyes.png");
	private final RenderCreeperween renderEntity;


	public LayerCreeperweenEyes(RenderCreeperween renderEntity)
	{
		this.renderEntity = renderEntity;
	}


	@Override
	public void doRenderLayer(EntityCreeperween entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.renderEntity.bindTexture(CREEPERWEEN_EYES);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(true);
		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderEntity.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		i = entity.getBrightnessForRender();
		j = i % 65536;
		k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
		this.renderEntity.setLightmap(entity);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}

}