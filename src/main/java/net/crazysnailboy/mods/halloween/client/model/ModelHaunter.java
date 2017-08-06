package net.crazysnailboy.mods.halloween.client.model;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelHaunter extends ModelBase
{

	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;

	public float brightness;
	public float opacity;
	public float onGround;


	public ModelHaunter()
	{
		this(0.0F, 6.0F);
	}

	public ModelHaunter(float modelSize, float p_i1149_2_)
	{
		this.opacity = 1.0F;

		this.bipedHead = new ModelRenderer(this, 33, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -3.0F, 8, 26, 6, modelSize);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -3.0F, 8, 26, 6, modelSize + 0.85F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		if (this.opacity > 0F)
		{
			//GL11.glEnable(2977 /*GL_NORMALIZE*/);
			GL11.glEnable(3042 /*GL_BLEND*/);

			float a = this.opacity;
			if (this.opacity > 1.0F)
			{
				this.opacity = 1.0F;
			}
			float b = ((this.brightness * 0.45F) + 0.55F);

			GL11.glColor4f(b, b, b, a);
			this.bipedHead.render(scale);

			GL11.glColor4f(1F, 1F, 1F, a);
			this.bipedHeadwear.render(scale);
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		this.bipedHead.rotateAngleY = netHeadYaw / 57.29578F;
		this.bipedHead.rotateAngleX = 0F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
	}

}
