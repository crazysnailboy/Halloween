package net.crazysnailboy.mods.halloween.util;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class WorldUtils
{

	public static <T extends Entity> T getClosestEntity(World world, Class<? extends T> classEntity, BlockPos pos, double distance)
	{
		double d0 = -1.0D;
		T closest = null;

		List<T> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(pos).expandXyz(distance));
		if (!entities.isEmpty())
		{
			for (T entity : entities)
			{
				double d1 = entity.getDistanceSq(pos);
				if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0))
				{
					d0 = d1;
					closest = entity;
				}
			}
		}
		return closest;
	}


//	public static <T extends Entity> T getClosestEntityExcluding(
//		World world, Class<? extends T> classEntity, BlockPos pos, double distance, @Nullable Predicate<T> predicate)
//	{
//		double d0 = -1.0D;
//		T closest = null;
//
//		List<T> entities = world.getEntities(classEntity, predicate);
//		if (!entities.isEmpty())
//		{
//			for (T entity : entities)
//			{
//				double d1 = entity.getDistanceSq(pos);
//				if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0))
//				{
//					d0 = d1;
//					closest = entity;
//				}
//			}
//		}
//		return closest;
//	}



//	public static <T extends Entity> T getClosestEntity(World world, Class<? extends T> classEntity, BlockPos pos, double distance,
//		@Nullable Predicate<T> predicate)
//	{
//
//	}

}
