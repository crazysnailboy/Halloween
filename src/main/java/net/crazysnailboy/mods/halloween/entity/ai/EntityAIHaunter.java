package net.crazysnailboy.mods.halloween.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter.EnumTransparencyState;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeCreeper;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeHusk;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSkeleton;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSpider;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeStray;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.crazysnailboy.mods.halloween.util.WorldUtils;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSnow;


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
	public static class Attack extends EntityAIBase
	{

		private final EntityHaunter taskOwner;
		private int attackStep;
		private int attackTime;
		private EntityPlayer attackTarget;
		private EntityMob currentDrone;
		private int droneAge;


		public Attack(EntityHaunter taskOwner)
		{
			this.taskOwner = taskOwner;
			this.setMutexBits(3);
		}

		@Override
		public boolean shouldExecute()
		{
			EntityLivingBase attackTarget = this.taskOwner.getAttackTarget();
			return (attackTarget != null && attackTarget.isEntityAlive() && attackTarget instanceof EntityPlayer);
		}

		@Override
		public void startExecuting()
		{
			this.attackTarget = (EntityPlayer)this.taskOwner.getAttackTarget();
			this.attackStep = 0;
		}

		@Override
		public void resetTask()
		{
			this.taskOwner.setCycleVisibility(1);
			this.taskOwner.setTransparencyState(EnumTransparencyState.TRANSPARENT);
		}

		@Override
		public void updateTask()
		{
			this.attackTime--;
			if (this.currentDrone != null) this.droneAge++;

			double distanceSq = this.taskOwner.getDistanceSqToEntity(attackTarget);
			if (distanceSq < 4.0D)
			{
				if (this.attackTime <= 0)
				{
					this.attackTime = 20;
					this.taskOwner.attackEntityAsMob(attackTarget);
				}

				this.taskOwner.getMoveHelper().setMoveTo(attackTarget.posX, attackTarget.posY, attackTarget.posZ, 1.0D);
			}
			else if (distanceSq < 256.0D)
			{
				// if the attack timer has run down...
				if (this.attackTime <= 0)
				{
					// increment the attack step
					++this.attackStep;

					switch (this.attackStep)
					{
						// if the attack step is 1...
						case 1:
							// fade the haunter in, and delay by 60 ticks before proceeding to step 2
							this.taskOwner.setCycleVisibility(51);
							this.attackTime = 60;
							break;

						// if the attack step is 2...
						case 2:
							// attempt the drone attack
							startDroneAttack();
							this.attackTime = 20;

						// if the attack step is 3...
						case 3:
							// fade the haunter out, and reset the attack step and timer
							this.taskOwner.setCycleVisibility(1);
							this.attackTime = 100;
							this.attackStep = 0;

					}
				}

				this.taskOwner.getLookHelper().setLookPositionWithEntity(attackTarget, 10.0F, 10.0F);
			}
			else
			{
				this.taskOwner.getNavigator().clearPathEntity();
				this.taskOwner.getMoveHelper().setMoveTo(attackTarget.posX, attackTarget.posY, attackTarget.posZ, 1.0D);
			}
			super.updateTask();
		}


		private void startDroneAttack()
		{
			if (this.currentDrone != null)
			{
				if (this.droneAge >= 600)
				{
					this.currentDrone.setDead();
				}
				if (this.currentDrone.isDead)
				{
					this.currentDrone = null;
				}
			}

			if (this.currentDrone == null)
			{
				this.currentDrone = getDrone();
				if (this.currentDrone == null) this.currentDrone = createDrone();

				if (this.currentDrone != null)
				{
					if (this.initializeDrone(this.currentDrone))
					{
//						this.currentDrone.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 300, 0));
						this.droneAge = 0;
					}
				}
			}
		}


		/**
		 * Find the nearest hostile mob to the player
		 */
		private EntityMob getDrone()
		{
			EntityMob entity = WorldUtils.getRandomEntity(this.taskOwner.world, EntityMob.class, this.attackTarget.getPosition(), 16.0D, new Predicate<EntityMob>()
			{
				@Override
				public boolean apply(@Nullable EntityMob entity)
				{
					return !(entity instanceof EntityHaunter);
				}
			});
			return entity;
		}

		/**
		 * Spawns a hostile mob in the vicinity of the target player which is then used to attack the player.
		 */
		private EntityMob createDrone()
		{
			EntityMob entity = null;
			Random rand = this.taskOwner.getRNG();
			World world = this.taskOwner.world;
			BlockPos pos = this.taskOwner.getPosition();

			// create a drone entity of a random type, with a chance of it being a fake
			int entityType = rand.nextInt(4);
			boolean fakeEntity = (((EntityHaunter)this.taskOwner).dronesProduced > 4 + rand.nextInt(5));

			switch (entityType)
			{
				case 0:
					boolean spawnHusk = (world.getBiome(pos) instanceof BiomeDesert && world.canSeeSky(pos) && rand.nextInt(5) != 0);
					entity = (!fakeEntity
						?
						(spawnHusk ? new EntityHusk(world) : new EntityZombie(world))
						:
						(spawnHusk ? new EntityFakeHusk(world) : new EntityHusk(world))
					);
					break;
				case 1:
					boolean spawnStray = (world.getBiome(pos) instanceof BiomeSnow && world.canSeeSky(pos) && rand.nextInt(5) != 0);
					entity = (!fakeEntity
						?
						(spawnStray ? new EntityStray(world) : new EntitySkeleton(world))
						:
						(spawnStray ? new EntityFakeStray(world) : new EntityFakeSkeleton(world))
					);
					break;
				case 2:
					entity = (!fakeEntity ? new EntitySpider(world) : new EntityFakeSpider(world));
					break;
				case 3:
					entity = (!fakeEntity ? new EntityCreeper(world) : new EntityFakeCreeper(world));
					break;
				default:
					return null;
			}

			// attempt to place the drone near to the player
			double x = this.attackTarget.posX;
			double y = this.attackTarget.posY;
			double z = this.attackTarget.posZ;

			// make 32 attempts to find a valid spawn location for the drone...
			for (int i = 0; i < 32; i++)
			{
				// pick a random position close to the target player
				double tryX = x + (16.0D - (rand.nextFloat() * 32.0D));
				double tryY = y + ((rand.nextFloat() - rand.nextFloat()) * 4.0D);
				double tryZ = z + (16.0D - (rand.nextFloat() * 32.0D));

				// if the closen position is a valid spawn location...
				if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(SpawnPlacementType.ON_GROUND, this.taskOwner.world, new BlockPos(tryX, tryY, tryZ)))
				{
					// set the drone's position and direction, and spawn it in the world
					entity.setPositionAndRotation(tryX, tryY, tryZ, MathHelper.wrapDegrees(rand.nextFloat() * 360.0F), 0.0F);
					entity.rotationYawHead = entity.rotationYaw;
					entity.renderYawOffset = entity.rotationYaw;
					entity.onInitialSpawn(this.taskOwner.world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
					world.spawnEntity(entity);

					// create a particle effect and play the appear sound
					entity.spawnExplosionParticle();
					entity.playSound(ModSoundEvents.ENTITY_HAUNTER_APPEAR, 5.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.1F) + 1.0F);

					this.taskOwner.setCycleVisibility(1);
					this.taskOwner.setTransparencyState(EnumTransparencyState.TRANSPARENT);


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
		private boolean initializeDrone(EntityMob mob)
		{
			// ensure that the drone has sufficient follow range to be able to path to the player
			double followRange = mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			float distance = mob.getDistanceToEntity(this.attackTarget);
			if (followRange < distance)
			{
				mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(distance * 1.1D);
			}

			// set the drone on a path to the player
			boolean canNavigate = mob.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.currentDrone.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
			if (canNavigate)
			{
				// set the drone's attack target to be the player
				mob.setAttackTarget(this.attackTarget);
			}
			else
			{
				this.currentDrone = null;
			}

			return canNavigate;
		}

	}

}