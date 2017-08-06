package net.crazysnailboy.mods.halloween.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater.EnumTreaterMessage;
import net.crazysnailboy.mods.halloween.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	 * EntityTreater will move towards the nearest EntityItem containing an ItemStack of ModItems.CANDY or ModItems.MEGA_CANDY.
	 * Derived from {@link net.minecraft.entity.ai.EntityAIFindEntityNearest}
	 */
	public static class MoveToNearestCandy extends EntityAIBase
	{

		private final EntityTreater taskOwner;
		private final Predicate<EntityItem> predicate;
		private final Sorter sorter;
		private final double moveSpeed;
		private EntityItem targetEntity;


		public MoveToNearestCandy(EntityTreater taskOwner, double moveSpeed)
		{
			this.taskOwner = taskOwner;
			this.moveSpeed = moveSpeed;

			this.predicate = new Predicate<EntityItem>()
			{
				@Override
				public boolean apply(@Nullable EntityItem entity)
				{
					double maxDistance = MoveToNearestCandy.this.getTargetDistance();

					if (MoveToNearestCandy.this.taskOwner.getDistanceToEntity(entity) < maxDistance)
					{
						if (entity.getEntityItem().getItem() == ModItems.CANDY)
						{
							if (entity.getEntityItem().getMetadata() == MoveToNearestCandy.this.taskOwner.getTreaterType().getCandyType().getMetadata())
							{
								// TODO - the canEasilyReach method doesn't stop treaters from trying to reach entities they can't.
								if (entity.onGround) // && EntityAIMoveToNearestCandy.this.canEasilyReach(entity))
								{
									return true;
								}
							}
						}
					}
					return false;
				}
			};
			this.sorter = new Sorter(taskOwner);
			this.setMutexBits(1);
		}


		@Override
		public boolean shouldExecute()
		{
			List<EntityItem> list = this.taskOwner.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), this.predicate);
			if (!list.isEmpty())
			{
				Collections.sort(list, this.sorter);
				this.targetEntity = list.get(0);
				return true;
			}
			return false;
		}

		@Override
		public void startExecuting()
		{
			// the tryMoveToEntityLiving method is misnamed - the target entity can be of any type and doesn't need to derive from EntityLivingBase.
			this.taskOwner.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.moveSpeed);
			super.startExecuting();
		}

		@Override
		public boolean continueExecuting()
		{
			EntityItem entity = this.targetEntity;
			if (entity == null)
			{
				return false;
			}
			else if (entity.isDead)
			{
				return false;
			}
			else
			{
				double targetDistance = this.getTargetDistance();
				return (targetDistance < this.taskOwner.getDistanceToEntity(entity));
			}
		}

		@Override
		public void resetTask()
		{
			this.taskOwner.setAttackTarget((EntityLivingBase)null);
			super.startExecuting();
		}


		private double getTargetDistance()
		{
			IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
		}

		private AxisAlignedBB getTargetableArea(double targetDistance)
		{
			return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
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
					int x = pathpoint.xCoord - MathHelper.floor(entity.posX);
					int z = pathpoint.zCoord - MathHelper.floor(entity.posZ);
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
