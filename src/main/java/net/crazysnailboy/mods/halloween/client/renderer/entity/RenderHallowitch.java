package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.renderer.entity.layers.LayerWitchClothes;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;


public class RenderHallowitch extends RenderBiped<EntityLiving>
{

	private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/hallowitch/witch.png");


	public RenderHallowitch(RenderManager renderManager)
	{
		super(renderManager, new ModelBiped(), 0.3F);
		this.addLayer(new LayerWitchClothes(this));
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity)
	{
		return WITCH_TEXTURES;
	}

}
