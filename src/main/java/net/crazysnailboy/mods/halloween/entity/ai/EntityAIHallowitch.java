package net.crazysnailboy.mods.halloween.entity.ai;

import net.crazysnailboy.mods.halloween.entity.monster.EntityHallowitch;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;


/**
 * AI tasks for {@link EntityHallowitch}
 *
 */
public class EntityAIHallowitch
{

	/**
	 * Copied from {@link net.minecraft.entity.monster.EntityPigZombie$AIHurtByAggressor}
	 *
	 */
	public static class HurtByAggressor extends EntityAIHurtByTarget
	{
		public HurtByAggressor(EntityHallowitch taskOwner)
		{
			super(taskOwner, true, new Class[0]);
		}

		@Override
		protected void setEntityAttackTarget(EntityCreature entityCreature, EntityLivingBase entityLivingBase)
		{
			super.setEntityAttackTarget(entityCreature, entityLivingBase);
			if (entityCreature instanceof EntityHallowitch)
			{
				((EntityHallowitch)entityCreature).becomeAngryAt(entityLivingBase);
			}
		}
	}

	/**
	 * Copied from {@link net.minecraft.entity.monster.EntityPigZombie$AITargetAggressor}
	 *
	 */
	public static class TargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
	{
		public TargetAggressor(EntityHallowitch taskOwner)
		{
			super(taskOwner, EntityPlayer.class, true);
		}

		@Override
		public boolean shouldExecute()
		{
			return ((EntityHallowitch)this.taskOwner).isAngry() && super.shouldExecute();
		}
	}

}