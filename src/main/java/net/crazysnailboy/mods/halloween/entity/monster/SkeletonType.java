package net.crazysnailboy.mods.halloween.entity.monster;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;


public enum SkeletonType
{
	NORMAL,
	STRAY;


	public SoundEvent getAmbientSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_SKELETON_AMBIENT : SoundEvents.ENTITY_STRAY_AMBIENT);
	}

	public SoundEvent getHurtSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_SKELETON_HURT : SoundEvents.ENTITY_STRAY_HURT);
	}

	public SoundEvent getDeathSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_SKELETON_DEATH : SoundEvents.ENTITY_STRAY_DEATH);
	}

	public SoundEvent getStepSound()
	{
		return (this == NORMAL ? SoundEvents.ENTITY_SKELETON_STEP : SoundEvents.ENTITY_STRAY_STEP);
	}

}