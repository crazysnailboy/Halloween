package net.crazysnailboy.mods.halloween.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ModelTreaterCreeper extends ModelBiped
{

	public ModelRendererPyramid bipedSkirt;
	private boolean field_1279_h;
	private boolean field_1278_i;
	private float onGround;


	public ModelTreaterCreeper()
	{
		this(0.25F, 0F);
	}

	public ModelTreaterCreeper(float modelSize, float p_i1149_2_)
	{
		this.field_1279_h = false;
		this.field_1278_i = false;
		this.isSneak = false;

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		this.bipedBody = new ModelRenderer(this, 24, 16);
		this.bipedBody.addBox(-5.0F, 12.0F, -3.0F, 10, 10, 6, modelSize);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 32, 0);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 32, 0);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(5F, 2.0F + p_i1149_2_, 0.0F);
		this.bipedSkirt = new ModelRendererPyramid(this, 0, 16);
		this.bipedSkirt.addBox(-4F, 0.0F, -2F, 8, 12, 4, modelSize, 0F, 1.0F);
		this.bipedSkirt.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	}


	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.bipedHead.render(scale);
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedSkirt.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		this.bipedHead.rotateAngleY = netHeadYaw / 57.29578F;
		this.bipedHead.rotateAngleX = headPitch / 57.29578F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
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
			this.bipedSkirt.rotateAngleY = this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f6) * 3.141593F * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f6 = 1.0F - this.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = MathHelper.sin(f6 * 3.141593F);
			float f8 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX -= (double)f7 * 1.2D + (double)f8;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
		}
		if (this.isSneak)
		{
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedSkirt.rotateAngleX = 0.5F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedHead.rotationPointY = 1.0F;
		}
		else
		{
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedSkirt.rotateAngleX = 0.0F;
			this.bipedHead.rotationPointY = 0.0F;
		}
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

}