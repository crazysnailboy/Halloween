package net.crazysnailboy.mods.halloween.entity.effect;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
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
	}


	@Override
	public void performCurse()
	{
		super.performCurse();
		if (this.lifetime > 0 && this.victim != null)
		{
			// get a list of the mobs within a 50 block radius of the victim (can't use EntityMob because Slimes & Jumpkins don't extend that class)
			List<EntityLiving> entities = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D), new Predicate<EntityLiving>()
			{
				@Override
				public boolean apply(@Nullable EntityLiving entity)
				{
					return (entity instanceof IMob) && !(entity instanceof EntityHaunter);
				}
			});

			// for each entity in the bounding box...
			for ( EntityLiving entity : entities )
			{
				// if it's more than 4 blocks from the victim and not currently pathing somewhere else...
				float distance = entity.getDistanceToEntity(this.victim);
				if (distance > 4.0F && entity.getNavigator().noPath())
				{
					// attempt to make it path to or near the victim
					this.roamNear(entity, distance);
				}
			}
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_GHAST_SCREAM, 2.0F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.GHAST;
	}



	private void roamNear(EntityLiving entity, float distance)
	{
		// if the entity is capable of pathing directly to the vicim...
		if (distance <= entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue())
		{
			// do that
			entity.getNavigator().tryMoveToEntityLiving(this.victim, 1.0D);
		}
		// otherwise...
		else
		{
			// determine a base target location to use as a base for the entity's pathing location
			double distanceX = this.victim.posX - entity.posX;
			double distanceZ = this.victim.posZ - entity.posZ;
			double crazy = Math.atan2(distanceX, distanceZ) + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.75D);

			int x = MathHelper.floor(entity.posX + (Math.sin(crazy) * 8.0F));
			int y = MathHelper.floor(entity.posY);
			int z = MathHelper.floor(entity.posZ + (Math.cos(crazy) * 8.0F));

			// make up to 16 attempts to find a valid location
			for (int i = 0; i < 16; i++)
			{
				// pick a location near our base target
				BlockPos pos = new BlockPos(x + this.rand.nextInt(4) - this.rand.nextInt(4), y + this.rand.nextInt(3) - this.rand.nextInt(3), z + this.rand.nextInt(4) - this.rand.nextInt(4));

				// if it's a valid location
				if (pos.getY() > 4 && pos.getY() < 255 && this.isPassable(pos) && !this.isPassable(pos.down()))
				{
					// tell the entity to try to path to it
					if (entity.getNavigator().tryMoveToXYZ(pos.getX(),pos.getY(), pos.getZ(), 1.0D))
					{
						// and break if the attempt succeeded
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