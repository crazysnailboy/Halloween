package net.crazysnailboy.mods.halloween.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class WorldUtils
{

	private static <T extends Entity> T getClosestEntity(List<T> entities, final BlockPos pos)
	{
		if (!entities.isEmpty())
		{
			Collections.sort(entities, new Comparator<T>()
			{
				@Override
				public int compare(T entityA, T entityB)
				{
					double d0 = pos.distanceSq(entityA.getPosition());
					double d1 = pos.distanceSq(entityB.getPosition());
					return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
				}
			});
			return entities.get(0);
		}
		return null;
	}

	private static <T extends Entity> T getRandomEntity(List<T> entities, final BlockPos pos)
	{
		if (!entities.isEmpty())
		{
			return entities.get(new Random().nextInt(entities.size()));
		}
		return null;
	}


	public static <T extends Entity> T getClosestEntity(final World world, final Class<? extends T> classEntity, final AxisAlignedBB aabb, Predicate<? super T> filter)
	{
		List<T> entities = world.getEntitiesWithinAABB(classEntity, aabb, filter);
		return getClosestEntity(entities, new BlockPos(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D));
	}

	public static <T extends Entity> T getClosestEntity(final World world, final Class<? extends T> classEntity, final BlockPos pos, double distance, Predicate<? super T> filter)
	{
		List<T> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(pos).grow(distance, distance, distance), filter);
		return getClosestEntity(entities, pos);
	}

	public static <T extends Entity> T getClosestEntity(final World world, final Class<? extends T> classEntity, final BlockPos pos, double distance)
	{
		return getClosestEntity(world, classEntity, pos, distance, EntitySelectors.NOT_SPECTATING);
	}

	public static <T extends Entity> T getClosestEntity(final World world, final Class<? extends T> classEntity, final AxisAlignedBB aabb)
	{
		return getClosestEntity(world, classEntity, aabb, EntitySelectors.NOT_SPECTATING);
	}


	public static <T extends Entity> T getRandomEntity(final World world, final Class<? extends T> classEntity, final BlockPos pos, double distance, Predicate<? super T> filter)
	{
		List<T> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(pos).grow(distance, distance, distance), filter);
		return getRandomEntity(entities, pos);
	}

}