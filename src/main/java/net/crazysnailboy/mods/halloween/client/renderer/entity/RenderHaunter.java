package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.model.ModelHaunter;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderHaunter extends RenderLiving<EntityHaunter>
{

    private static final ResourceLocation HAUNTER_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/entity/haunter/haunter.png");
    private final ModelHaunter haunterModel;


    public RenderHaunter(RenderManager renderManager)
	{
    	this(renderManager, new ModelHaunter(), 0.0F);
	}

    private RenderHaunter(RenderManager renderManager, ModelHaunter haunterModel, float shadowSize)
    {
    	super(renderManager, haunterModel, shadowSize);
    	this.haunterModel = haunterModel;
    }


    @Override
	protected void preRenderCallback(EntityHaunter entity, float partialTickTime)
	{
//    	switch (entity.transparencyState)
//    	{
//    		case TRANSPARENT:
//    			entity.opacity = 0.0F;
//    			break;
//    		case FADING_IN:
//    			entity.opacity += 0.035F;
//    			if (entity.opacity > 1.0F) entity.opacity = 1.0F;
//    			break;
//    		case OPAQUE:
//    			entity.opacity = 1.0F;
//    			break;
//    		case FADING_OUT:
//    			entity.opacity -= 0.0275F;
//    			if (entity.opacity < 0.0F) entity.opacity = 0.0F;
//    			break;
//    	}

//		System.out.println("preRenderCallback :: { cycleVisibility=" + entity.cycleVisibility + ", transparencyState=" + entity.transparencyState + " , opacity=" + entity.opacity + " }");

		this.haunterModel.opacity = entity.getOpacity();
		this.haunterModel.brightness = entity.getBrightness(1.0F);


//		((ModelHaunter)this.mainModel).opacity = entity.opacity;
//		((ModelHaunter)this.mainModel).brightness = entity.getBrightness(1.0F);
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityHaunter entity)
	{
		return HAUNTER_TEXTURES;
	}

}
