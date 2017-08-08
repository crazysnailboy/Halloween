package net.crazysnailboy.mods.halloween.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;


public class EntitySpiderCurse extends EntityCurse
{

	public EntitySpiderCurse(World world)
	{
		this(world, null);
	}

	public EntitySpiderCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}


	@Override
	public void performCurse()
	{
		super.performCurse();
		this.lift = 8;
		if (this.lifetime > 0 && this.victim != null)
		{
			this.victim.motionY += 0.3D;
			double angle = this.rand.nextFloat() * (Math.PI * 2.0D);
			this.victim.motionX += Math.sin(angle) * 0.375F;
			this.victim.motionZ += Math.cos(angle) * 0.375F;
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_SPIDER_HURT, 0.75F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.SPIDER;
	}

}