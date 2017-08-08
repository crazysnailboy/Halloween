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

	private void roamNear(EntityLivingBase entityLivingBase, EntityCreature entityCreature, float distance)
	{
		if (distance <= 14.0F)
		{
			entityCreature.getNavigator().tryMoveToEntityLiving(entityLivingBase, 1.0D);
		}
		else
		{
			double distanceX = entityLivingBase.posX - entityCreature.posX;
			double distanceZ = entityLivingBase.posZ - entityCreature.posZ;

			double arctan = Math.atan2(distanceX, distanceZ) + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.75D);

			int x = MathHelper.floor(entityCreature.posX + (Math.sin(arctan) * 8.0F));
			int y = MathHelper.floor(entityCreature.getEntityBoundingBox().minY);
			int z = MathHelper.floor(entityCreature.posZ + (Math.cos(arctan) * 8.0F));

			for (int q = 0; q < 16; q++)
			{
				BlockPos pos = new BlockPos(x + this.rand.nextInt(4) - this.rand.nextInt(4), y + this.rand.nextInt(3) - this.rand.nextInt(3), this.rand.nextInt(4) - this.rand.nextInt(4));
				if (pos.getY() > 4 && pos.getY() < 255 && this.isPassable(pos) && !this.isPassable(pos.down()))
				{
					EntityLivingBase entity = WorldUtils.getClosestEntity(this.world, EntityLivingBase.class, pos, 16.0F);
					if (entity != null && entityCreature.getNavigator().tryMoveToEntityLiving(entity, 1.0D))
					{
						break;
					}
				}
			}
		}
	}

	private boolean isPassable(BlockPos pos)
	{
		return this.world.getBlockState(pos).getBlock().isPassable(this.world, pos);
	}

}