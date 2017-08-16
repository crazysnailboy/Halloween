package net.crazysnailboy.mods.halloween.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
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

		if (this.opacity > 0.0F)
		{
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend(); // GL11.glEnable(3042 /*GL_BLEND*/);

			if (this.opacity > 1.0F) this.opacity = 1.0F;

			float gray = ((this.brightness * 0.45F) + 0.55F);
			GlStateManager.color(gray, gray, gray, this.opacity); // GL11.glColor4f(gray, gray, gray, this.opacity);
			this.bipedHead.render(scale);

			GlStateManager.color(1.0F, 1.0F, 1.0F, this.opacity); // GL11.glColor4f(1.0F, 1.0F, 1.0F, this.opacity);
			this.bipedHeadwear.render(scale);

			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		this.bipedHead.rotateAngleY = netHeadYaw / (180.0F / (float)Math.PI);
		this.bipedHead.rotateAngleX = 0.0F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
	}

}