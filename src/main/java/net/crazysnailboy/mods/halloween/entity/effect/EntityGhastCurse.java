package net.crazysnailboy.mods.halloween.entity.effect;

import java.util.List;
import net.crazysnailboy.mods.halloween.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class EntityGhastCurse extends EntityCurse
{

	public EntityGhastCurse(World world)
	{
		this(world, null);
	}

	public EntityGhastCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
		this.lifetime = 1800;
	}

	@Override
	public void performCurse()
	{
		super.performCurse();
		this.swell = 10;
		if (this.lifetime > 0 && this.victim != null)
		{
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(50D, 50D, 50D));
			for ( Entity entity : list )
			{
				if (entity instanceof EntityMob && entity != this.victim)
				{
					float distance = entity.getDistanceToEntity(this.victim);
					EntityMob mob = (EntityMob)entity;
					if (!mob.hasPath() && distance > 4.0F)
					{
						this.roamNear(this.victim, mob, distance);
					}
				}
			}
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_GHAST_SCREAM, 2.0F, (this.rand.nextFloat() * 0.4F) + 0.8F); // "mob.ghast.scream"
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.GHAST;
	}


	private void roamNear(EntityLivingBase lure, EntityCreature creature, float distance)
	{
		if (distance <= 14.0F)
		{
			creature.getNavigator().tryMoveToEntityLiving(lure, 1.0D);
//			PathEntity dogs = world.getPathToEntity(creature, lure, 16F);
//			if (dogs != null)
//			{
//				creature.setPathToEntity(dogs);
//			}
		}
		else
		{
			double a = lure.posX - creature.posX;
			double b = lure.posZ - creature.posZ;
			double crazy = Math.atan2(a, b);
			crazy += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75D;
			double c = creature.posX + (Math.sin(crazy) * 8F);
			double d = creature.posZ + (Math.cos(crazy) * 8F);

			int x = MathHelper.floor(c);
			int y = MathHelper.floor(creature.getEntityBoundingBox().minY);
			int z = MathHelper.floor(d);

			for (int q = 0; q < 16; q++)
			{
				BlockPos pos = new BlockPos(x + this.rand.nextInt(4) - this.rand.nextInt(4), y + this.rand.nextInt(3) - this.rand.nextInt(3), this.rand.nextInt(4) - this.rand.nextInt(4));
				if (pos.getY() > 4 && pos.getY() < 255 && this.isPassable(pos) && !this.isPassable(pos.down()))
				{
					EntityLivingBase lure1 = WorldUtils.getClosestEntity(this.world, EntityLivingBase.class, pos, 16.0F);
					if (creature.getNavigator().tryMoveToEntityLiving(lure1, 1.0D))
					{
						break;
					}
				}

//				int i = x + rand.nextInt(4) - rand.nextInt(4);
//				int j = y + rand.nextInt(3) - rand.nextInt(3);
//				int k = z + rand.nextInt(4) - rand.nextInt(4);

//				if (j > 4 && j < 254 && isAirySpace(i, j, k) && !isAirySpace(i, j - 1, k))
//				{
//					PathEntity dogs = world.getEntityPathToXYZ(creature, i, j, k, 16F);
//					if (dogs != null)
//					{
//						creature.setPathToEntity(dogs);
//						break;
//					}
//				}
			}
		}
	}

	private boolean isPassable(BlockPos pos)
	{
		return this.world.getBlockState(pos).getBlock().isPassable(this.world, pos);
	}

}
