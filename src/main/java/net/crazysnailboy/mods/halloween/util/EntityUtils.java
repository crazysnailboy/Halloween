package net.crazysnailboy.mods.halloween.util;

import java.lang.reflect.Field;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;


public class EntityUtils
{



	public static boolean getCanSpawnHere(EntityLivingBase entity, EnumCreatureType creatureType)
	{
		// default spawning logic for instances of EntityLiving
		boolean canLivingSpawnHere = entity.world.getBlockState((new BlockPos(entity)).down()).canEntitySpawn(entity);
		boolean canCreatureSpawnHere = false;
		boolean canMonsterSpawnHere = false;

		if (canLivingSpawnHere)
		{
			// default spawning logic for instances of EntityCreature
			float blockPathWeight = 0.0F;
			if (entity instanceof EntityCreature)
			{
				BlockPos pos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
				blockPathWeight = ((EntityCreature)entity).getBlockPathWeight(pos);
			}
			canCreatureSpawnHere = (blockPathWeight >= 0.0F);
			if (creatureType == EnumCreatureType.CREATURE) return canCreatureSpawnHere;
		}

		if (canCreatureSpawnHere && creatureType == EnumCreatureType.MONSTER)
		{
			// default spawning logic for instances of EntityMob
			if (entity.world.getDifficulty() != EnumDifficulty.PEACEFUL)
			{
				BlockPos pos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
				if (entity.world.getLightFor(EnumSkyBlock.SKY, pos) <= entity.world.rand.nextInt(32))
				{
					int i = entity.world.getLightFromNeighbors(pos);
					if (entity.world.isThundering())
					{
						int j = entity.world.getSkylightSubtracted();
						entity.world.setSkylightSubtracted(10);
						i = entity.world.getLightFromNeighbors(pos);
						entity.world.setSkylightSubtracted(j);
					}
					canMonsterSpawnHere = (i <= entity.world.rand.nextInt(8));
				}
			}
			return canMonsterSpawnHere;
		}

		return canLivingSpawnHere;
	}




	/**
	 * Checks whether the spawn conditions support spawning of creatures.
	 * Adapted from net.minecraft.entity.monster.EntityMob#getCanSpawnHere
	 *
	 */
	public static boolean getCanMobSpawnHere(EntityLivingBase entity)
	{
		return entity.world.getDifficulty() != EnumDifficulty.PEACEFUL && EntityMob_isValidLightLevel(entity) && getCanCreatureSpawnHere(entity);
	}

	/**
	 * Checks whether the spawn conditions support spawning of creatures.
	 * Adapted from net.minecraft.entity.EntityCreature#getCanSpawnHere
	 *
	 */
	private static boolean getCanCreatureSpawnHere(EntityLivingBase entity)
	{
		return getCanLivingSpawnHere(entity) && EntityCreature_getBlockPathWeight(new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ)) >= 0.0F;
	}

	/**
	 * Checks whether the spawn conditions support spawning of living entities.
	 * Adapted from {@link net.minecraft.entity.EntityLiving#getCanSpawnHere()}
	 *
	 */
	private static boolean getCanLivingSpawnHere(EntityLivingBase entity)
	{
		IBlockState state = entity.world.getBlockState((new BlockPos(entity)).down());
		return state.canEntitySpawn(entity);
	}


	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 * Adapted from {@link net.minecraft.entity.monster.EntityMob#isValidLightLevel()}
	 *
	 */
	private static boolean EntityMob_isValidLightLevel(EntityLivingBase entity)
	{
		BlockPos pos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
		if (entity.world.getLightFor(EnumSkyBlock.SKY, pos) > entity.world.rand.nextInt(32))
		{
			return false;
		}
		else
		{
			int i = entity.world.getLightFromNeighbors(pos);
			if (entity.world.isThundering())
			{
				int j = entity.world.getSkylightSubtracted();
				entity.world.setSkylightSubtracted(10);
				i = entity.world.getLightFromNeighbors(pos);
				entity.world.setSkylightSubtracted(j);
			}
			return i <= entity.world.rand.nextInt(8);
		}
	}

	private static float EntityCreature_getBlockPathWeight(BlockPos pos)
	{
		return 0.0F;
	}

	public static int getFire(Entity entity)
	{
		return ReflectionUtils.getFieldValue(entityFire, entity);
	}

	private static final Field entityFire = ReflectionUtils.getDeclaredField(Entity.class, "fire", "field_70151_c");

}
