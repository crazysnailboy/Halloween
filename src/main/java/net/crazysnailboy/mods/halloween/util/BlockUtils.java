package net.crazysnailboy.mods.halloween.util;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockUtils
{

	public static boolean isSoftGround(World world, BlockPos pos)
	{
		Material material = world.getBlockState(pos).getMaterial();
		return (material == Material.GRASS || material == Material.GROUND || material == Material.SAND);
	}

}