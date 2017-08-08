package net.crazysnailboy.mods.halloween.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;


public class EntityZombieCurse extends EntityCurse
{

	public EntityZombieCurse(World world)
	{
		this(world, null);
	}

	public EntityZombieCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.lifetime > 0 && this.victim != null)
		{
			this.victim.motionX *= 0.625D;
			this.victim.motionZ *= 0.625D;

			if (!this.victim.onGround && this.victim.motionY > 0D)
			{
				this.victim.motionY -= 0.06D;
			}
		}
	}

	@Override
	public void performCurse()
	{
		super.performCurse();
		this.dash = 8;
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 0.75F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.ZOMBIE;
	}

}