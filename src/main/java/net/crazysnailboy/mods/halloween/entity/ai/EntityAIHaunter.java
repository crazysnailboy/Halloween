package net.crazysnailboy.mods.halloween.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter.EnumTransparencyState;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeCreeper;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSkeleton;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSpider;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeZombie;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldEntitySpawner;


/**
 * AI tasks for {@link EntityHaunter}
 *
 */
public class EntityAIHaunter
{

	/**
	 * Haunters will detect players within a 64 block range,
	 * and either take over a nearby hostile mob or spawn a new one, and make that mob attack the player.
	 */
	public static class Attack extends EntityAITarget
	{

		protected final Attack.Sorter sorter;
		protected EntityPlayer targetPlayer;
		private EntityMob currentDrone;
		protected final EntityHaunter haunter;
	    protected int runDelay;

		private final Predicate<EntityPlayer> predicate;

		private final Predicate<EntityMob> mobPredicate = new Predicate<EntityMob>()
		{
			@Override
			public boolean apply(@Nullable EntityMob entity)
			{
				return (Attack.this.taskOwner != entity);
			}
		};


		public Attack(EntityHaunter taskOwner)
		{
			this(taskOwner, true, (Predicate<EntityPlayer>)null);
		}

		private Attack(EntityHaunter taskOwner, boolean checkSight, @Nullable final Predicate<EntityPlayer> targetSelector)
		{
			super(taskOwner, checkSight, false);
			this.haunter = taskOwner;
			this.predicate = new Predicate<EntityPlayer>()
			{
				@Override
				public boolean apply(@Nullable EntityPlayer player)
				{
					return player == null ? false : (targetSelector != null && !targetSelector.apply(player) ? false : (!EntitySelectors.NOT_SPECTATING.apply(player) ? false : Attack.this.isSuitableTarget(player, false)));
				}
			};
			this.sorter = new Attack.Sorter(this.taskOwner);
			this.setMutexBits(1);
		}


		/**
		 * Returns true if there's an attackable player within the mob's target range.
		 */
		@Override
		public boolean shouldExecute()
		{
	        if (this.runDelay > 0)
	        {
	            --this.runDelay;
	            return false;
	        }
	        else
	        {
	            this.runDelay = 200 + this.taskOwner.getRNG().nextInt(200);

				this.targetPlayer = this.taskOwner.world.getNearestAttackablePlayer(
					this.taskOwner.posX, this.taskOwner.posY + (double)this.taskOwner.getEyeHeight(), this.taskOwner.posZ,
					this.getTargetDistance(), this.getTargetDistance(),
					null, this.predicate);

				return (this.targetPlayer != null);
	        }
		}

		@Override
		public void startExecuting()
		{
			this.taskOwner.setAttackTarget(this.targetPlayer);
			super.startExecuting();
		}

		@Override
		public void updateTask()
		{
			super.updateTask();

			// if our current drone is dead, remove it as a drone.
			if (this.currentDrone != null && this.currentDrone.isDead)
			{
				this.currentDrone = null;
			}

			// if we don't have a current drone,
			// either attempt to take over the nearest available hostile mob or spawn one if there isn't one available
			if (this.currentDrone == null) this.currentDrone = getNearestAvailableDrone();
			if (this.currentDrone == null) this.currentDrone = createDrone();

			// make the selected mob attack the player
			if (this.currentDrone != null) this.initializeDrone(this.currentDrone);

//			this.currentDrone.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 300, 0));

		}


		private AxisAlignedBB getTargetableArea(double targetDistance)
		{
			return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
		}

		/**
		 * Find the nearest hostile mob to the player
		 */
		private EntityMob getNearestAvailableDrone()
		{
			List<EntityMob> list = this.taskOwner.world.<EntityMob>getEntitiesWithinAABB(EntityMob.class, this.getTargetableArea(this.getTargetDistance()), mobPredicate);
			if (!list.isEmpty())
			{
				Collections.sort(list, this.sorter);
				return list.get(0);
			}
			return null;
		}

		/**
		 * Spawns a hostile mob in the vicinity of the target player which is then used to attack the player.
		 */
		private EntityMob createDrone()
		{
			EntityMob entity = null;
			Random rand = this.taskOwner.world.rand;

			// create a drone entity of a random type, with a chance of it being a fake
			int entityType = rand.nextInt(4);
			boolean fakeEntity = (((EntityHaunter)this.taskOwner).dronesProduced > 4 + rand.nextInt(5));

			switch (entityType)
			{
				case 0:
					entity = (!fakeEntity ? new EntityZombie(this.taskOwner.world) : new EntityFakeZombie(this.taskOwner.world));
					break;
				case 1:
					entity = (!fakeEntity ? new EntitySkeleton(this.taskOwner.world) : new EntityFakeSkeleton(this.taskOwner.world));
					break;
				case 2:
					entity = (!fakeEntity ? new EntitySpider(this.taskOwner.world) : new EntityFakeSpider(this.taskOwner.world));
					break;
				case 3:
					entity = (!fakeEntity ? new EntityCreeper(this.taskOwner.world) : new EntityFakeCreeper(this.taskOwner.world));
					break;
				default:
					return null;
			}

			// attempt to place the drone near to the player
			double x = this.targetPlayer.posX;
			double y = this.targetPlayer.posY;
			double z = this.targetPlayer.posZ;

			// make 32 attempts to find a valid spawn location for the drone...
			for (int i = 0; i < 32; i++)
			{
				// pick a random position close to the target player
				double tryX = x + (16.0D - (rand.nextFloat() * 32.0D));
				double tryY = y + ((rand.nextFloat() - rand.nextFloat()) * 4.0D);
				double tryZ = z + (16.0D - (rand.nextFloat() * 32.0D));
				BlockPos pos = new BlockPos(tryX, tryY, tryZ);

				// if the closen position is a valid spawn location...
				if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(SpawnPlacementType.ON_GROUND, this.taskOwner.world, pos))
				{
					// set the drone's position and direction, and spawn it in the world
					entity.setPositionAndRotation(tryX, tryY, tryZ, MathHelper.wrapDegrees(rand.nextFloat() * 360.0F), 0.0F);
					entity.rotationYawHead = entity.rotationYaw;
					entity.renderYawOffset = entity.rotationYaw;
					entity.onInitialSpawn(this.taskOwner.world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
					this.taskOwner.world.spawnEntity(entity);

					// create a particle effect and play the appear sound
					entity.spawnExplosionParticle();
					entity.playSound(ModSoundEvents.ENTITY_HAUNTER_APPEAR, 5.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.1F) + 1.0F);

					this.haunter.setCycleVisibility(1);
					this.haunter.setTransparencyState(EnumTransparencyState.TRANSPARENT);


					// increment the counter for the number of drones produced
					((EntityHaunter)this.taskOwner).dronesProduced++;

					// return the entity
					return entity;
				}
			}

			return null;
		}

		/**
		 * Make the drone mob attack the target player
		 */
		private void initializeDrone(EntityMob mob)
		{
			// ensure that the drone has sufficient follow range to be able to path to the player
			double followRange = mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			if (followRange < mob.getDistanceToEntity(this.targetPlayer))
			{
				mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(followRange * 1.1D);
			}

			// set the drone on a path to the player
			if (mob.getNavigator().tryMoveToEntityLiving(this.targetPlayer, this.currentDrone.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()))
			{
				// set the drone's attack target to be the player
				mob.setAttackTarget(this.targetPlayer);
			}
		}


		/**
		 * Comparator for sorting a collection of {@link Entity} objects by distance from a specified target {@link Entity}.
		 * TODO - this class is duplicated - consolidate.
		 */
		public static class Sorter implements Comparator<Entity>
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

}