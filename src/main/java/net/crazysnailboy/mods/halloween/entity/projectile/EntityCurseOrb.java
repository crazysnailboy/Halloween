package net.crazysnailboy.mods.halloween.entity.projectile;

import net.crazysnailboy.mods.halloween.entity.effect.EntityCreeperCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse.EnumCurseType;
import net.crazysnailboy.mods.halloween.entity.effect.EntityGhastCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySkeletonCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySlimeCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySpiderCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityZombieCurse;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHallowitch;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityCurseOrb extends EntityThrowable
{

	private EntityLivingBase thrower;


	public EntityCurseOrb(World world)
	{
		super(world);
	}

	public EntityCurseOrb(World world, EntityLivingBase thrower)
	{
		super(world, thrower);
		this.thrower = thrower;
	}

	public EntityCurseOrb(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}


	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (!this.world.isRemote)
		{
			// if the curse orb hit a living entity which is not a haunter...
			if (result.entityHit != null && result.entityHit instanceof EntityLivingBase && !(result.entityHit instanceof EntityHaunter))
			{
				// create and spawn a random curse, with the entity which was hit as the curse's victim
				EntityLivingBase victim = (EntityLivingBase)result.entityHit;
				EntityCurse curse = null;

				switch (EnumCurseType.getRandom())
				{
					case CREEPER: curse = new EntityCreeperCurse(this.world, victim); break;
					case GHAST: curse = new EntityGhastCurse(this.world, victim); break;
					case SKELETON: curse = new EntitySkeletonCurse(this.world, victim); break;
					case SLIME: curse = new EntitySlimeCurse(this.world, victim); break;
					case SPIDER: curse = new EntitySpiderCurse(this.world, victim); break;
					case ZOMBIE: curse = new EntityZombieCurse(this.world, victim); break;
				}
				this.world.spawnEntity(curse);

				// if the curse orb was thrown by a witch...
				if (this.getThrower() instanceof EntityHallowitch)
				{
					// make the witch disappear in a puff of particles
					EntityHallowitch witch = (EntityHallowitch)this.getThrower();
					witch.onKillCommand();
					witch.spawnExplosionParticle();
				}
			}

			// kill the curse orb
			this.world.setEntityState(this, (byte)3); // used by EntitySnowball to spawn particles
			this.setDead();
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.world.isRemote && this.thrower == player && this.inGround && this.throwableShake <= 0)
		{
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this, 1);
			this.setDead();
		}
	}

}