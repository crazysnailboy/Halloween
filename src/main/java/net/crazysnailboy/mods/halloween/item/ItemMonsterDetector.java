package net.crazysnailboy.mods.halloween.item;

import java.util.List;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;


public class ItemMonsterDetector extends Item
{

	private int timeUntil;


	public ItemMonsterDetector()
	{
		super();
	}


	/**
	 * Check whether there are any mobs in the vicinity, and play a sound if there are.
	 * The sound played changes if the mob is targeting the player, and the sound gets more frequent the closer the mob is to the player.
	 */
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if (!world.isRemote && entity instanceof EntityPlayer)
		{
			if (this.timeUntil > 0)
			{
				this.timeUntil--;
				return;
			}

			EntityPlayer player = (EntityPlayer)entity;

			double distance = 100.0D;
			boolean scary = false;

			List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D));
			for (EntityMob mob : mobs)
			{
				double mobDistance = mob.getDistanceToEntity(player);
				if (mobDistance < distance)
				{
					distance = mobDistance;
					if (mob.getAttackTarget() != null && mob.getAttackTarget() == player)
					{
						scary = true;
					}
				}
			}

			if (distance <= 32.0D)
			{
				SoundEvent soundEvent = (scary ? ModSoundEvents.ITEM_MONSTER_DETECTOR_HIGH : ModSoundEvents.ITEM_MONSTER_DETECTOR_LOW);
				world.playSound((EntityPlayer)null, player.getPosition(), soundEvent, SoundCategory.NEUTRAL, 0.75F, 1.0F);

				if (distance >= 24.0D)
				{
					this.timeUntil = 40;
				}
				else if (distance >= 18.0D)
				{
					this.timeUntil = 20;
				}
				else if (distance >= 12.0D)
				{
					this.timeUntil = 15;
				}
				else if (distance >= 6.0D)
				{
					this.timeUntil = 10;
				}
				else
				{
					this.timeUntil = 7;
				}
			}
			else this.timeUntil = 80;


		}
	}

}