package net.crazysnailboy.mods.halloween.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class WorldUtils
{

	public static <T extends Entity> T getClosestEntity(final World world, final Class<? extends T> classEntity, final BlockPos pos, double distance)
	{
		List<T> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(pos).expand(distance, distance, distance));
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

	public static Entity getEntityByUUID(World world, UUID uuid)
	{
		for ( Entity entity : world.loadedEntityList)
		{
			if (uuid.equals(entity.getUniqueID()))
			{
				return entity;
			}
		}
		return null;
	}

	public static <T extends Entity> T getEntityByUUID(World world, Class<? extends T> classEntity, UUID uuid)
	{
		for ( T entity : world.getEntities(classEntity, EntitySelectors.IS_ALIVE))
		{
			if (uuid.equals(entity.getUniqueID()))
			{
				return entity;
			}
		}
		return null;
	}

}