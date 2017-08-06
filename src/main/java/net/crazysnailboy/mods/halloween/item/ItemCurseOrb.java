package net.crazysnailboy.mods.halloween.item;

import net.crazysnailboy.mods.halloween.entity.projectile.EntityCurseOrb;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCurseOrb extends Item
{
	public ItemCurseOrb()
	{
		this.setMaxStackSize(8);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (!player.capabilities.isCreativeMode)
		{
			stack.shrink(1);
		}

		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote)
		{
			EntityCurseOrb entity = new EntityCurseOrb(world, player);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(entity);
		}

//		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}


	@SideOnly(Side.CLIENT)
	public static class ColorHandler implements IItemColor
	{
		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex)
		{
			return 0xFF00AA;
		}
	}

}
