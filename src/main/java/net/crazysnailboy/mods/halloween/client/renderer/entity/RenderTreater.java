package net.crazysnailboy.mods.halloween.client.renderer.entity;

import java.util.Collections;
import java.util.List;
import com.google.common.collect.Lists;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.model.ModelTreaterSpider;
import net.crazysnailboy.mods.halloween.client.renderer.entity.layers.LayerTreaterCreeper;
import net.crazysnailboy.mods.halloween.client.renderer.entity.layers.LayerWitchClothes;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater.EnumTreaterType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderTreater extends RenderLiving<EntityTreater>
{

    private static final ResourceLocation TREATER_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/treater/treater.png");
    private static final ResourceLocation SKELETON_TREATER_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/treater/treater_skeleton.png");
	private static final ResourceLocation SPIDER_TREATER_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/treater/treater_spider.png");
    private static final ResourceLocation ZOMBIE_TREATER_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/treater/treater_zombie.png");

    private final ModelBase defaultTreaterModel;
    private final ModelTreaterSpider spiderTreaterModel;

    private final List<LayerRenderer<EntityTreater>> defaultLayers;
    private final List<LayerRenderer<EntityTreater>> creeperLayers;
    private final List<LayerRenderer<EntityTreater>> witchLayers;


	public RenderTreater(RenderManager renderManager)
	{
		this(renderManager, new ModelBiped());
	}

	private RenderTreater(RenderManager renderManager, ModelBase model)
	{
		super(renderManager, model, 0.25F);

		this.defaultTreaterModel = this.mainModel;
		this.spiderTreaterModel = new ModelTreaterSpider();

		this.defaultLayers = Lists.newArrayList(this.layerRenderers);
		this.creeperLayers = Collections.singletonList((LayerRenderer<EntityTreater>)new LayerTreaterCreeper(this));
		this.witchLayers = Collections.singletonList((LayerRenderer<EntityTreater>)new LayerWitchClothes(this));
	}


	@Override
	protected void preRenderCallback(EntityTreater entity, float partialTickTime)
	{
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
	}

	@Override
	public void doRender(EntityTreater entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.switchModel(entity);
		this.switchLayerRenderers(entity);

		if (entity.getTreaterType() != EnumTreaterType.SPIDER)
		{
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
		else
		{
			super.doRender(entity, x, y, z, entityYaw, partialTicks * 0.375F);
		}
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityTreater entity)
	{
		switch (entity.getTreaterType())
		{
			case SKELETON: return SKELETON_TREATER_TEXTURES;
			case SPIDER: return SPIDER_TREATER_TEXTURES;
			case ZOMBIE: return ZOMBIE_TREATER_TEXTURES;

			default: return TREATER_TEXTURES;
		}
	}

	private void switchModel(EntityTreater entity)
	{
		this.mainModel = (entity.getTreaterType() == EnumTreaterType.SPIDER ? this.spiderTreaterModel : this.defaultTreaterModel);
	}

	private void switchLayerRenderers(EntityTreater entity)
	{
		switch (entity.getTreaterType())
		{
			case CREEPER: this.layerRenderers = this.creeperLayers; break;
			case WITCH: this.layerRenderers = this.witchLayers; break;

			default: this.layerRenderers = this.defaultLayers; break;
		}
	}

}