package net.crazysnailboy.mods.halloween.entity.monster;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;


public enum ZombieType
{
	NORMAL,
	HUSK;


	public SoundEvent getAmbientSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_ZOMBIE_AMBIENT : SoundEvents.ENTITY_HUSK_AMBIENT);
	}

	public SoundEvent getHurtSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_ZOMBIE_HURT : SoundEvents.ENTITY_HUSK_HURT);
	}

	public SoundEvent getDeathSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_ZOMBIE_DEATH : SoundEvents.ENTITY_HUSK_DEATH);
	}

	public SoundEvent getStepSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_ZOMBIE_STEP : SoundEvents.ENTITY_HUSK_STEP);
	}

}