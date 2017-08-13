package net.crazysnailboy.mods.halloween.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater.EnumTreaterMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;


/**
 * AI tasks for {@link EntityTreater}
 *
 */
public class EntityAITreater
{

	/**
	 * EntityTreater will move towards the nearest EntityItem containing an ItemStack of ModItems.MEGA_CANDY or ModItems.CANDY.
	 * Derived from {@link net.minecraft.entity.ai.EntityAIFindEntityNearest}
	 */
	public static class MoveToNearestCandy extends EntityAIBase
	{

		private final EntityTreater taskOwner;
		private final Sorter sorter;
		private final double moveSpeed;
		private EntityItem targetEntity;
		private int runDelay;


		public MoveToNearestCandy(EntityTreater taskOwner, double moveSpeed)
		{
			this.taskOwner = taskOwner;
			this.moveSpeed = moveSpeed;
			this.sorter = new Sorter(taskOwner);
			this.setMutexBits(1);
		}


		@Override
		public boolean shouldExecute()
		{
			// wait a random amount of time before attempting to execute
			if (this.runDelay > 0)
			{
				--this.runDelay;
				return false;
			}
			this.runDelay = 40 + this.taskOwner.getRNG().nextInt(20);

			// only execute if mobGriefing is enabled...
			if (this.taskOwner.world.getGameRules().getBoolean("mobGriefing"))
			{
				// and there's candy in the vicinity
				return this.searchForDestination();
			}
			return false;
		}

		@Override
		public void startExecuting()
		{
			// attempt to move towards the candy
			this.taskOwner.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.moveSpeed); // the tryMoveToEntityLiving method is misnamed - the target entity can be of any type and doesn't need to derive from EntityLivingBase.
			super.startExecuting();
		}

		@Override
		public boolean shouldContinueExecuting()
		{
			EntityItem entity = this.targetEntity;

			// don't execute if the target candy is no longer available
			if (entity == null || entity.isDead)
			{
				return false;
			}

			// only execute if the target candy is within the targetable area
			return (this.getTargetDistance() < this.taskOwner.getDistanceToEntity(entity));
		}

		@Override
		public void resetTask()
		{
			super.resetTask();
		}


		private double getTargetDistance()
		{
			IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
		}

		private AxisAlignedBB getTargetableArea(double targetDistance)
		{
			// .expand() in 1.10 is the same as .grow() 1.11 & 1.12. in 1.11 & 1.12, .expand() is the same as .addCoord() in 1.10
			return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
		}

		/**
		 * Attempt to find an EntityItem containing an ItemStack of ModItems.MEGA_CANDY or ModItems.CANDY which the Treater can pick up
		 */
		private boolean searchForDestination()
		{
			// search for candy items in the vicinity of the treater that the treater can pick up
			List<EntityItem> list = this.taskOwner.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), new Predicate<EntityItem>()
			{
				@Override
				public boolean apply(@Nullable EntityItem entity)
				{
					double maxDistance = MoveToNearestCandy.this.getTargetDistance();
					EntityTreater taskOwner = MoveToNearestCandy.this.taskOwner;

					if (entity.onGround && taskOwner.canTreaterPickupItem(entity.getItem()))
					{
						if (taskOwner.getDistanceToEntity(entity) < maxDistance)
						{
							//return MoveToNearestCandy.this.canEasilyReach(entity); // TODO - the canEasilyReach method doesn't stop treaters from trying to reach entities they can't.
							return true;
						}
					}
					return false;
				}
			});

			// if we've found any...
			if (!list.isEmpty())
			{
				// select the closest one as the target and return true
				Collections.sort(list, this.sorter);
				this.targetEntity = list.get(0);
				return true;
			}
			// otherwise
			else
			{
				// clear the target and return false
				this.targetEntity = null;
				return false;
			}
		}

		/**
		 * Checks to see if this entity can find a short path to the given target.
		 * Copied from {@link net.minecraft.entity.ai.EntityAITarget#canEasilyReach}.
		 *
		 */
		private boolean canEasilyReach(EntityItem entity)
		{
			Path path = this.taskOwner.getNavigator().getPathToEntityLiving(entity);
			if (path != null)
			{
				PathPoint pathpoint = path.getFinalPathPoint();
				if (pathpoint != null)
				{
					int x = pathpoint.x - MathHelper.floor(entity.posX);
					int z = pathpoint.z - MathHelper.floor(entity.posZ);
					return (double)(x * x + z * z) <= 2.25D;
				}
			}
			return false;
		}


		/**
		 * Sorts a collection of Entities in ascending order of their distance from the target Entity.
		 * Copied from {@link net.minecraft.entity.ai.EntityAINearestAttackableTarget$Sorter}
		 *
		 */
		private static class Sorter implements Comparator<Entity>
		{

			private final Entity entity;

			public Sorter(Entity entity)
			{
				this.entity = entity;
			}

			@Override
			public int compare(Entity entityA, Entity entityB)
			{
				double d0 = this.entity.getDistanceSqToEntity(entityA);
				double d1 = this.entity.getDistanceSqToEntity(entityB);
				return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
			}
		}

	}


	/**
	 * EntityTreater will look at and greet the nearest EntityPlayer.
	 * Extended from {@link net.minecraft.entity.ai.EntityAIWatchClosest}.
	 */
	public static class WatchClosestPlayer extends net.minecraft.entity.ai.EntityAIWatchClosest
	{

		private final EntityTreater taskOwner;


		public WatchClosestPlayer(EntityTreater taskOwner, float maxDistance)
		{
			super(taskOwner, EntityPlayer.class, maxDistance);
			this.taskOwner = taskOwner;
		}

		@Override
		public void startExecuting()
		{
			super.startExecuting();
			this.greetPlayer();
		}


		private void greetPlayer()
		{
			if (this.closestEntity != null)
			{
				EntityPlayer closestPlayer = (EntityPlayer)this.closestEntity;
				this.taskOwner.chatItUp(closestPlayer, EnumTreaterMessage.GREETING);
			}
		}

	}

}