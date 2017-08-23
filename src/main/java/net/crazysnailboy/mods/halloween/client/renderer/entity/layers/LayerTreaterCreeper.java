package net.crazysnailboy.mods.halloween.client.renderer.entity.layers;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.model.ModelTreaterCreeper;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderTreater;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class LayerTreaterCreeper<T extends ModelBase> implements LayerRenderer<EntityTreater>
{
	private static final ResourceLocation TEXTURE_COSTUME = new ResourceLocation(HalloweenMod.MODID, "textures/entity/treater/treater_creeper.png");

	private final ModelTreaterCreeper model = new ModelTreaterCreeper(0.25F, 0F);
	private final RenderTreater renderEntity;


	public LayerTreaterCreeper(RenderTreater renderEntity)
	{
		this.renderEntity = renderEntity;
	}


	@Override
	public void doRenderLayer(EntityTreater entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.renderEntity.bindTexture(TEXTURE_COSTUME);

		GlStateManager.pushMatrix();
		this.model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}

}