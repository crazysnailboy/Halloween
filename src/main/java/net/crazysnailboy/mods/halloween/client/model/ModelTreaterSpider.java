package net.crazysnailboy.mods.halloween.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ModelTreaterSpider extends ModelBiped
{

	public ModelRenderer bipedRightArm2;
	public ModelRenderer bipedLeftArm2;
	public ModelRenderer bipedRightArm3;
	public ModelRenderer bipedLeftArm3;


	public ModelTreaterSpider()
	{
		this(0.0F, 0.0F);
	}

	public ModelTreaterSpider(float modelSize, float p_i1149_2_)
	{
		super(modelSize, p_i1149_2_, 64, 32);

		this.bipedRightArm2 = new ModelRenderer(this, 56, 16);
		this.bipedRightArm2.addBox(-1.0F, -1.0F, -1.0F, 2, 10, 2, modelSize);
		this.bipedRightArm2.setRotationPoint(-4.0F, 5.0F + p_i1149_2_, 0.0F);

		this.bipedLeftArm2 = new ModelRenderer(this, 56, 16);
		this.bipedLeftArm2.mirror = true;
		this.bipedLeftArm2.addBox(-1.0F, -1.0F, -1.0F, 2, 10, 2, modelSize);
		this.bipedLeftArm2.setRotationPoint(4.0F, 5.0F + p_i1149_2_, 0.0F);

		this.bipedRightArm3 = new ModelRenderer(this, 56, 16);
		this.bipedRightArm3.addBox(-1.0F, -1.0F, -1.0F, 2, 10, 2, modelSize);
		this.bipedRightArm3.setRotationPoint(-4.0F, 9.0F + p_i1149_2_, 0.0F);

		this.bipedLeftArm3 = new ModelRenderer(this, 56, 16);
		this.bipedLeftArm3.mirror = true;
		this.bipedLeftArm3.addBox(-1.0F, -1.0F, -1.0F, 2, 10, 2, modelSize);
		this.bipedLeftArm3.setRotationPoint(4.0F, 9.0F + p_i1149_2_, 0.0F);
	}


	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		this.bipedRightArm2.render(scale);
		this.bipedLeftArm2.render(scale);
		this.bipedRightArm3.render(scale);
		this.bipedLeftArm3.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		this.bipedRightArm2.rotateAngleZ = this.bipedRightArm3.rotateAngleZ = this.bipedRightArm.rotateAngleZ = 1.0F;
		this.bipedRightArm2.rotateAngleZ -= 0.15F;
		this.bipedRightArm3.rotateAngleZ -= 0.3F;
		this.bipedRightArm2.rotateAngleX = this.bipedRightArm3.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.25F;
		this.bipedRightArm2.rotateAngleX *= -1.0F;
		this.bipedRightArm2.rotateAngleY = this.bipedRightArm3.rotateAngleY = this.bipedRightArm.rotateAngleY;

		this.bipedLeftArm2.rotateAngleZ = this.bipedLeftArm3.rotateAngleZ = this.bipedLeftArm.rotateAngleZ = -1.0F;
		this.bipedLeftArm2.rotateAngleZ += 0.15F;
		this.bipedLeftArm3.rotateAngleZ += 0.3F;
		this.bipedLeftArm2.rotateAngleX = this.bipedLeftArm3.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.25F;
		this.bipedLeftArm2.rotateAngleX *= -1.0F;
		this.bipedLeftArm2.rotateAngleY = this.bipedLeftArm3.rotateAngleY = this.bipedLeftArm.rotateAngleY;
	}

}