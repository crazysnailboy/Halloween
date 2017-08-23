package net.crazysnailboy.mods.halloween.client.model;

import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse.EnumCurseType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ModelCurse extends ModelBase
{

	private ModelRenderer head;


	public ModelCurse()
	{
		this(null);
	}

	public ModelCurse(EnumCurseType curseType)
	{
		if (curseType == EnumCurseType.GHAST)
		{
			this.head = new ModelRenderer(this, 0, 0);
			this.head.addBox(-8.0F, -12.0F, -8.0F, 16, 16, 16, -4.0F);
		}
		else if (curseType == EnumCurseType.SPIDER)
		{
			this.head = new ModelRenderer(this, 32, 4);
			this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		}
		else if (curseType == EnumCurseType.ZOMBIE)
		{
			this.head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
			this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		}
		else
		{
			this.head = new ModelRenderer(this, 0, 0);
			this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		}
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
	}


	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.head.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		this.head.rotateAngleY = 0.0F;
		this.head.rotateAngleX = 0.05F;
	}

}