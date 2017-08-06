package net.crazysnailboy.mods.halloween.entity.projectile;

import net.crazysnailboy.mods.halloween.entity.effect.EntityCreeperCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse.EnumCurseType;
import net.crazysnailboy.mods.halloween.entity.effect.EntityGhastCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySkeletonCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySlimeCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySpiderCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityZombieCurse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityCurseOrb extends EntityThrowable
{

	public EntityCurseOrb(World world)
    {
        super(world);
    }

	public EntityCurseOrb(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

	public EntityCurseOrb(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }


	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (result.entityHit != null)
		{
			if (result.entityHit instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)result.entityHit;

				EntityCurse curse = null;
				EnumCurseType curseType = EnumCurseType.getRandom();

				switch (curseType)
				{
					case CREEPER: curse = new EntityCreeperCurse(this.world, player); break;
					case GHAST: curse = new EntityGhastCurse(this.world, player); break;
					case SKELETON: curse = new EntitySkeletonCurse(this.world, player); break;
					case SLIME: curse = new EntitySlimeCurse(this.world, player); break;
					case SPIDER: curse = new EntitySpiderCurse(this.world, player); break;
					case ZOMBIE: curse = new EntityZombieCurse(this.world, player); break;
				}
				this.world.spawnEntity(curse);

			}
		}
	}


	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (this.inGround && this.getThrower() == player && this.throwableShake <= 0)
		{
//			worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this, 1);
			this.setDead();
		}
	}

}
