package net.crazysnailboy.mods.halloween.item;

import java.util.List;
import java.util.Random;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.item.ItemCandy.EnumCandyFlavour;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemCandy extends ItemFood implements IMultiItem<EnumCandyFlavour>
{

	public ItemCandy()
	{
		super(2, 0.3F, false);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}


	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + EnumCandyFlavour.byMetadata(stack.getMetadata()).getUnlocalizedName();
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		List<EntityCurse> entities = world.getEntitiesWithinAABB(EntityCurse.class, player.getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D));
		for (EntityCurse entity : entities)
		{
			if (entity.victim == player)
			{
				entity.setDead();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (EnumCandyFlavour value : EnumCandyFlavour.values())
			{
				subItems.add(new ItemStack(this, 1, value.getMetadata()));
			}
		}
	}

	public enum EnumCandyFlavour implements ISubItem
	{
		WATERMELON(0, "red", "", 0xC3614C),
		APPLE(1, "green", "", 0x41CD34),
		LEMON(2, "yellow", "", 0xDECF2A),
		PUMPKIN(3, "orange", "", 0xEB8844),
		GRAPE(4, "purple", "", 0x7B2FBE);

		private static final EnumCandyFlavour[] META_LOOKUP = new EnumCandyFlavour[values().length];

		private final int meta;
		private final String unlocalizedName;
		private final ResourceLocation registryName;
		private final int color;

		private EnumCandyFlavour(int meta, String unlocalizedName, String registryName, int color)
		{
			this.meta = meta;
			this.unlocalizedName = unlocalizedName;
			this.registryName = new ResourceLocation(HalloweenMod.MODID, registryName);
			this.color = color;
		}


		@Override
		public ResourceLocation getRegistryName()
		{
			return this.registryName;
		}

		@Override
		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		@Override
		public int getMetadata()
		{
			return this.meta;
		}

		public int getColor()
		{
			return this.color;
		}

		public static EnumCandyFlavour getRandom()
		{
			return META_LOOKUP[new Random().nextInt(values().length)];
		}


		public static EnumCandyFlavour byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static
		{
			for (EnumCandyFlavour value : values())
			{
				META_LOOKUP[value.getMetadata()] = value;
			}
		}

	}


	@SideOnly(Side.CLIENT)
	public static class ColorHandler implements IItemColor
	{
		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex)
		{
			return EnumCandyFlavour.byMetadata(stack.getMetadata()).getColor();
		}
	}

}