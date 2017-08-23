package net.crazysnailboy.mods.halloween.client.renderer.entity.layers;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.model.ModelWitchClothes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class LayerWitchClothes<T extends ModelBase> implements LayerRenderer<EntityLiving>
{
	private static final ResourceLocation TEXTURE_COSTUME = new ResourceLocation(HalloweenMod.MODID, "textures/entity/hallowitch/witch_clothes.png");

	private final ModelWitchClothes model = new ModelWitchClothes();
	private final RenderLiving renderEntity;


    public LayerWitchClothes(RenderLiving renderEntity)
    {
    	this.renderEntity = renderEntity;
    }


	@Override
	public void doRenderLayer(EntityLiving entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
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