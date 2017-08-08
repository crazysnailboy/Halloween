package net.crazysnailboy.mods.halloween.client.model;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelZombieHands extends ModelBiped
{

    public ModelZombieHands()
    {
        this(0.0F, 24.0F, 64, 64);
    }

    public ModelZombieHands(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn)
	{
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;

		this.isRiding = false;
		this.isChild = false;
		this.isSneak = false;
		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-2F, 0F, 0F, 4, 12, 4, modelSize);
		this.bipedRightArm.setRotationPoint(-4F, 0F + p_i1149_2_, 2.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-2F, 0F, 0F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(4F, 0F + p_i1149_2_, 2.0F);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		GL11.glTranslatef(0F, 0.375F - (limbSwingAmount * 0.75F), 0F);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)
	{
		this.bipedRightArm.rotateAngleX = -3.141593F + (MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * limbSwingAmount * 0.375F) + (limbSwingAmount);
		this.bipedLeftArm.rotateAngleX = -3.141593F + (MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.375F) + (limbSwingAmount);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;

		this.bipedRightArm.rotateAngleY = 0.1F;
		this.bipedLeftArm.rotateAngleY = -0.0F;

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

}