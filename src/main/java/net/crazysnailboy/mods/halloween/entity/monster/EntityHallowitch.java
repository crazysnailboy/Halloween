package net.crazysnailboy.mods.halloween.entity.monster;

import net.crazysnailboy.mods.halloween.entity.ai.EntityAIHallowitch;
import net.crazysnailboy.mods.halloween.entity.projectile.EntityCurseOrb;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class EntityHallowitch extends EntityMob implements IRangedAttackMob
{

	private boolean isSwinging;
	private boolean gotTarget;
	private int swingProgressInt;
	private int angerLevel;


	public EntityHallowitch(World world)
	{
		super(world);
		this.setSize(0.6F, 1.8F);
		this.setAIMoveSpeed(0.5F);
	}


	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHallowitch.HurtByAggressor(this)); // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAIHallowitch.TargetAggressor(this)); // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	/**
	 * Adapted from {@link net.minecraft.entity.monster.EntityWitch#attackEntityWithRangedAttack(EntityLivingBase, float)}
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		double x = target.posX + target.motionX - this.posX;
		double z = target.posZ + target.motionZ - this.posZ;
		double y = ((target.posY + (double)target.getEyeHeight() - 1.1D) - this.posY) + (double)(MathHelper.sqrt(x * x + z * z) * 0.2F);

		EntityCurseOrb entity = new EntityCurseOrb(this.world, this);
		entity.rotationPitch -= -20.0F;
		entity.setThrowableHeading(x, y, z, 0.75F, 8.0F);

		this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
		this.world.spawnEntity(entity);
	}

	public void becomeAngryAt(EntityLivingBase entity)
	{
		this.angerLevel = 400 + this.rand.nextInt(400);
		this.setRevengeTarget(entity);
	}

	public boolean isAngry()
	{
		return this.angerLevel > 0;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return ModSoundEvents.ENTITY_HALLOWITCH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return ModSoundEvents.ENTITY_HALLOWITCH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_HALLOWITCH_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLootTables.ENTITIES_HALLOWITCH;
	}

	@Override
	public void setSwingingArms(boolean swingingArms)
	{
	}


	public void gotchaBitch(EntityLivingBase entity)
	{
		if (entity != null && this.getAttackTarget() != null && entity == this.getAttackTarget())
		{
			this.onKillCommand();
			this.spawnExplosionParticle();
		}
	}

}