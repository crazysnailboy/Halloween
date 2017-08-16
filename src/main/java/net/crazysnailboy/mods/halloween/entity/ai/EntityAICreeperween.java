package net.crazysnailboy.mods.halloween.entity.ai;

import net.crazysnailboy.mods.halloween.entity.monster.EntityCreeperween;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;


/**
 * AI tasks for {@link EntityCreeperween}
 *
 */
public class EntityAICreeperween
{

	/**
	 * Copied from {@link net.minecraft.entity.ai.EntityAICreeperSwell}.
	 * Changed to swell an instance of EntityCreeperween rather than EntityCreeper.
	 *
	 */
	public static class Swell extends EntityAIBase
	{

		EntityCreeperween taskOwner;
		EntityLivingBase attackTarget;


		public Swell(EntityCreeperween taskOwner)
		{
			this.taskOwner = taskOwner;
			this.setMutexBits(1);
		}


		@Override
		public boolean shouldExecute()
		{
			EntityLivingBase attackTarget = this.taskOwner.getAttackTarget();
			return (this.taskOwner.getCreeperState() > 0 || attackTarget != null && this.taskOwner.getDistanceSqToEntity(attackTarget) < 9.0D);
		}

		@Override
		public void startExecuting()
		{
			this.taskOwner.getNavigator().clearPathEntity();
			this.attackTarget = this.taskOwner.getAttackTarget();
		}

		@Override
		public void resetTask()
		{
			this.attackTarget = null;
		}

		@Override
		public void updateTask()
		{
			if (this.attackTarget == null)
			{
				this.taskOwner.setCreeperState(-1);
			}
			else if (this.taskOwner.getDistanceSqToEntity(this.attackTarget) > 49.0D)
			{
				this.taskOwner.setCreeperState(-1);
			}
			else if (!this.taskOwner.getEntitySenses().canSee(this.attackTarget))
			{
				this.taskOwner.setCreeperState(-1);
			}
			else
			{
				this.taskOwner.setCreeperState(1);
			}
		}
	}

}