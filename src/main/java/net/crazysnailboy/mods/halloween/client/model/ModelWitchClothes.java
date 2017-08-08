package net.crazysnailboy.mods.halloween.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelWitchClothes extends ModelBiped
{

	public ModelRendererPyramid bipedHat;
	public ModelRendererPyramid bipedCoat1;
	public ModelRendererPyramid bipedCoat2;
	public ModelRenderer bipedCross1;
	public ModelRenderer bipedCross2;
	private boolean field_1279_h;
	private boolean field_1278_i;
	private float onGround;


	public ModelWitchClothes()
	{
		this(0.0F, 0.0F, 64, 32);
	}

	public ModelWitchClothes(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn)
	{
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;


		this.bipedHat = new ModelRendererPyramid(this, 0, 14);
		this.bipedHat.addBox(-4.5F, -15.5F, -5.25F, 9, 9, 9, modelSize, -4.5F, 0.0F);
		this.bipedHat.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedHeadwear = new ModelRenderer(this, 28, 21);
		this.bipedHeadwear.addBox(-5.5F, -7F, -6.25F, 11, 1, 11, modelSize);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedCoat1 = new ModelRendererPyramid(this, 0, 5);
		this.bipedCoat1.addBox(-4.5F, -0.5F, -2.5F, 9, 3, 5, modelSize + 0.5F, 1F, 0F);
		this.bipedCoat1.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedCoat2 = new ModelRendererPyramid(this, 34, 5);
		this.bipedCoat2.addBox(-4.5F, 2.5F, -2.5F, 9, 10, 5, modelSize + 0.5F, 0F, 1F);
		this.bipedCoat2.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 1, 14);
		this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 4, 4, modelSize + 0.375F);
		this.bipedRightArm.setRotationPoint(-5F, 2.0F + p_i1149_2_, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 1, 14);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 4, 4, modelSize + 0.375F);
		this.bipedLeftArm.setRotationPoint(5F, 2.0F + p_i1149_2_, 0.0F);

		this.bipedCross1 = new ModelRenderer(this, 28, 21);
		this.bipedCross1.addBox(-1.125F, 3F, -4F, 4, 1, 1, modelSize - 0.3F);
		this.bipedCross1.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);

		this.bipedCross2 = new ModelRenderer(this, 28, 21);
		this.bipedCross2.addBox(-3.0F, 3F, -4F, 4, 1, 1, modelSize - 0.3F);
		this.bipedCross2.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	}


	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.bipedHat.render(scale);
		this.bipedCoat1.render(scale);
		this.bipedCoat2.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedHeadwear.render(scale);
		this.bipedCross1.render(scale);
		this.bipedCross2.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		this.bipedHat.rotateAngleY = netHeadYaw / 57.29578F;
		this.bipedHat.rotateAngleX = headPitch / 57.29578F;
		this.bipedHat.rotateAngleX -= 0.15F;
		this.bipedHeadwear.rotateAngleY = this.bipedHat.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHat.rotateAngleX;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;

		if (this.isRiding)
		{
			this.bipedRightArm.rotateAngleX += -0.6283185F;
			this.bipedLeftArm.rotateAngleX += -0.6283185F;
		}
		if (this.field_1279_h)
		{
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F;
		}
		if (this.field_1278_i)
		{
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.3141593F;
		}
		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
		if (this.onGround > -9990F)
		{
			float f6 = this.onGround;
			this.bipedCross1.rotateAngleY = this.bipedCross2.rotateAngleY = this.bipedCoat2.rotateAngleY = this.bipedCoat1.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f6) * 3.141593F * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedCoat1.rotateAngleY) * 5F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedCoat1.rotateAngleY) * 5F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedCoat1.rotateAngleY) * 5F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedCoat1.rotateAngleY) * 5F;
			this.bipedRightArm.rotateAngleY += this.bipedCoat1.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedCoat1.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedCoat1.rotateAngleY;
			f6 = 1.0F - this.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = MathHelper.sin(f6 * 3.141593F);
			float f8 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHat.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX -= (double)f7 * 1.2D + (double)f8;
			this.bipedRightArm.rotateAngleY += this.bipedCoat1.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
		}
		if (this.isSneak)
		{
			this.bipedCoat1.rotateAngleX = 0.5F;
			this.bipedCoat2.rotateAngleX = 0.5F;
			this.bipedCross1.rotateAngleX = 0.4F;
			this.bipedCross2.rotateAngleX = 0.4F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedHat.rotationPointY = 1.0F;
		}
		else
		{
			this.bipedCoat1.rotateAngleX = 0.0F;
			this.bipedCoat2.rotateAngleX = 0.0F;
			this.bipedCross1.rotateAngleX = -0.1F;
			this.bipedCross2.rotateAngleX = -0.1F;
			this.bipedHat.rotationPointY = 0.0F;
		}

		this.bipedCross1.rotateAngleZ = 0.45F;
		this.bipedCross2.rotateAngleZ = -0.45F;

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

}