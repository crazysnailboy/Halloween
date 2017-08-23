package net.crazysnailboy.mods.halloween.potion;

import javax.annotation.Nullable;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PotionCurse extends Potion
{

	private static final ResourceLocation ICON_TEXTURES = new ResourceLocation(HalloweenMod.MODID, "textures/gui/icons.png");


	public PotionCurse()
	{
		this(true, 0x000000);
	}

	public PotionCurse(boolean isBadEffect, int liquidColor)
	{
		super(isBadEffect, liquidColor);
	}


	@Override
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		if (this == ModPotions.CREEPER_CURSE)
		{
		}
		else if (this == ModPotions.GHAST_CURSE)
		{
		}
		else if (this == ModPotions.SKELETON_CURSE)
		{
		}
		else if (this == ModPotions.SPIDER_CURSE)
		{
		}
		else if (this == ModPotions.ZOMBIE_CURSE)
		{
		}
	}

	@Override
	public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entity, int amplifier, double health)
	{
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		super.renderInventoryEffect(x, y, effect, mc);

		mc.renderEngine.bindTexture(ICON_TEXTURES);
		Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 90, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		super.renderHUDEffect(x, y, effect, mc, alpha);

		mc.renderEngine.bindTexture(ICON_TEXTURES);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 90, 18);

	}

}