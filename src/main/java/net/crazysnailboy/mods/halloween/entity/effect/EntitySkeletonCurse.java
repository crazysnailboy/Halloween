package net.crazysnailboy.mods.halloween.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class EntitySkeletonCurse extends EntityCurse
{

	public EntitySkeletonCurse(World world)
	{
		this(world, null);
	}

	public EntitySkeletonCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}


	@Override
	public void performCurse()
	{
		super.performCurse();

		this.dash = 8;
		if (this.lifetime > 0 && this.victim != null)
		{
			float amount = (this.rand.nextInt(20) + 1);
			float health = this.victim.getHealth() - amount;

			if (health <= 0)
			{
				this.victim.setHealth(amount);
			}
			else
			{
				this.victim.attackEntityFrom(DamageSource.generic, health);
				this.victim.hurtTime = this.victim.maxHurtTime = 10;
			}
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_SKELETON_HURT, 0.75F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.SKELETON;
	}

}