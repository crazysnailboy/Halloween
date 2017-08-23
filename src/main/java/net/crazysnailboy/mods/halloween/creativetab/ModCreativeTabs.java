package net.crazysnailboy.mods.halloween.creativetab;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ModCreativeTabs
{

	public static final CreativeTabs HALLOWEEN = new CreativeTabs(HalloweenMod.MODID)
	{

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem()
		{
			return new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN));
		}
	};

}