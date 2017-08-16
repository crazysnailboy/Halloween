package net.crazysnailboy.mods.halloween.entity.monster;

import net.crazysnailboy.mods.halloween.entity.ai.EntityAIHaunter;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.crazysnailboy.mods.halloween.network.datasync.ModDataSerializers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Mostly adapted from {@link net.minecraft.entity.monster.EntityBlaze}.
 *
 */
public class EntityHaunter extends EntityMob
{

	private static final DataParameter<Float> OPACITY = EntityDataManager.<Float>createKey(EntityHaunter.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> CYCLE_VISIBILITY = EntityDataManager.<Integer>createKey(EntityHaunter.class, DataSerializers.VARINT);
	private static final DataParameter<EnumTransparencyState> TRANSPARENCY_STATE = EntityDataManager.<EnumTransparencyState>createKey(EntityHaunter.class, ModDataSerializers.HAUNTER_TRANSPARENCY);

	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;


	public EntityHaunter(World worldIn)
	{
		super(worldIn);
		this.setSize(0.7F, 1.6F);
		this.isImmuneToFire = true;
		this.experienceValue = 10;

		moveAngle = rand.nextFloat() * 360F;
		moveSpeed = rand.nextFloat() * 0.0125F + 0.035F;
		moveCurve = (rand.nextFloat() - rand.nextFloat()) * 0.075F;
	}


	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(CYCLE_VISIBILITY, 0);
		this.dataManager.register(OPACITY, 0.0F);
		this.dataManager.register(TRANSPARENCY_STATE, EnumTransparencyState.TRANSPARENT);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(4, new EntityAIHaunter.Attack(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected SoundEvent getHurtSound()
	{
		return ModSoundEvents.ENTITY_HAUNTER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_HAUNTER_DEATH;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialTicks)
	{
		return 15728880;
	}

	@Override
	public float getBrightness(float partialTicks)
	{
		return 1.0F;
	}

	@Override
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	/**
	 * Adapted from {@link net.minecraft.entity.boss.EntityWither#onLivingUpdate()} to make the haunter fly more like a Wither than a Blaze
	 */
	@Override
	public void onLivingUpdate()
	{

//		if (!this.onGround && this.motionY < 0.0D)
//		{
//			this.motionY *= 0.6D;
//		}

		this.motionY *= 0.6D;

		if (!this.world.isRemote && this.getAttackTarget() != null)
		{
			EntityLivingBase entity = this.getAttackTarget();

			if (this.posY < entity.posY + 2.0D)
			{
				if (this.motionY < 0.0D) this.motionY = 0.0D;
				this.motionY += (0.5D - this.motionY) * 0.6D;
			}

			double distanceX = entity.posX - this.posX;
			double distanceZ = entity.posZ - this.posZ;
			double distanceSq = distanceX * distanceX + distanceZ * distanceZ;

			if (distanceSq > 25.0D)
			{
				double distance = (double)MathHelper.sqrt(distanceSq);
				this.motionX += (distanceX / distance * 0.5D - this.motionX) * 0.6D;
				this.motionZ += (distanceZ / distance * 0.5D - this.motionZ) * 0.6D;
			}
			else if (distanceSq < 4.0D)
			{
				double distance = (double)MathHelper.sqrt(distanceSq);
				this.motionX -= (distanceX / distance * 0.5D - this.motionX) * 0.6D;
				this.motionZ -= (distanceZ / distance * 0.5D - this.motionZ) * 0.6D;
			}
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05D)
		{
			this.rotationYaw = (float)MathHelper.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
		}

//		if (this.world.isDaytime())
//		{
//			this.setDead();
//			this.spawnExplosionParticle();
//		}

		super.onLivingUpdate();
	}

	@Override
	protected void updateAITasks()
	{
		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}

		--this.heightOffsetUpdateTime;

		if (this.heightOffsetUpdateTime <= 0)
		{
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
		}

		EntityLivingBase attackTarget = this.getAttackTarget();
		if (attackTarget != null && attackTarget.posY + (double)attackTarget.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
		{
			this.motionY += (0.3D - this.motionY) * 0.3D;
			this.isAirBorne = true;
		}

		super.updateAITasks();
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLootTables.ENTITIES_HAUNTER;
	}

	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		// if the haunter wasn't damaged by an entity, or was damaged by a mob, do nothing
		Entity entity = source.getTrueSource();
		if (entity == null || entity instanceof EntityMob)
		{
			return false;
		}

		// check if the haunter took damage
		boolean takenDamage = super.attackEntityFrom(source, amount);

		// if the haunter took damage from a living entity...
		if (takenDamage && entity instanceof EntityLivingBase)
		{
			// spawn explosion particles
			this.spawnExplosionParticle();

			// if the haunter's run out of health
			if (this.getHealth() <= 0.0F)
			{
				// clear the attack target
				this.setAttackTarget(null);
				this.setCycleVisibility(51); // this.cycleVisibility = 51;
			}
			// otherwise (if the haunter hasn't run out of health)
			else
			{
				// set the attack target to the entity which caused it damage
				this.setAttackTarget((EntityLivingBase)entity);

				// if cycleVisibility is less than 85 (it's maximum), set it to 85
				if (this.getCycleVisibility() < 85) // if (this.cycleVisibility < 85)
				{
					this.setCycleVisibility(85); // this.cycleVisibility = 85;
					// this.setTransparencyState(EnumTransparencyState.OPAQUE); // this.transparencyState = EnumTransparencyState.OPAQUE;
				}

				// strafe left or right randomly
				this.setMoveStrafing(this.rand.nextInt(2) == 0 ? 1.5F : -1.5F);
			}
			// this.syncVisibilityToClients();
		}

		return takenDamage;
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();

		this.updateEntityVisibility();

//		if (this.cycleVisibility == 0)
//		{
////			if (child != null && health > 0)
////			{
////				child = null;
////				gotChild = false;
////				entcount = -30;
////			}
//		}

		if (this.deathTime >= 10)
		{
			this.deathTime = 10;
			if (this.getOpacity() < 0.01F) // if (this.opacity < 0.01F)
			{
				this.setDead();
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setFloat("MoveAngle", this.moveAngle);
		compound.setFloat("MoveSpeed", this.moveSpeed);
		compound.setFloat("MoveCurve", this.moveCurve);
		compound.setFloat("Opacity", this.getOpacity());
		compound.setInteger("CycleVisibility", this.getCycleVisibility());
		compound.setInteger("DronesProduced", this.dronesProduced);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		moveAngle = compound.getFloat("MoveAngle");
		moveSpeed = compound.getFloat("MoveSpeed");
		moveCurve = compound.getFloat("MoveCurve");
		this.setOpacity(compound.getFloat("Opacity"));
		this.setCycleVisibility(compound.getInteger("CycleVisibility"));
		this.dronesProduced = compound.getInteger("DronesProduced");
	}


	public float getOpacity()
	{
		return this.dataManager.get(OPACITY).floatValue();
	}

	public void setOpacity(float value)
	{
		this.dataManager.set(OPACITY, value);
	}

	public int getCycleVisibility()
	{
		return this.dataManager.get(CYCLE_VISIBILITY).intValue();
	}

	public void setCycleVisibility(int value)
	{
		this.dataManager.set(CYCLE_VISIBILITY, value);
	}

	public EnumTransparencyState getTransparencyState()
	{
		return this.dataManager.get(TRANSPARENCY_STATE);
	}

	public void setTransparencyState(EnumTransparencyState value)
	{
		this.dataManager.set(TRANSPARENCY_STATE, value);
	}



	/**
	 * Overridden and super method not called to allow this mob to pass through other entities.
	 */
	@Override
	public void applyEntityCollision(Entity entity)
	{
	}



// ====================================================================================================
// Everything below here is custom code adapted from KodaichiZero's original
//
// ====================================================================================================

	public enum EnumTransparencyState
	{
		TRANSPARENT((byte)0),
		FADING_IN((byte)1),
		OPAQUE((byte)2),
		FADING_OUT((byte)3);

		private final byte byteValue;

		private EnumTransparencyState(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return this.byteValue;
		}

		public static EnumTransparencyState getFromVisibility(int cycleVisibility)
		{
			if (cycleVisibility >= 85)
			{
				return EnumTransparencyState.OPAQUE;
			}
			else if (cycleVisibility == 84)
			{
				return EnumTransparencyState.TRANSPARENT;
			}
			else if (cycleVisibility > 50)
			{
				return EnumTransparencyState.FADING_OUT;
			}
			else if (cycleVisibility > 15)
			{
				return EnumTransparencyState.OPAQUE;
			}
			else if (cycleVisibility > 0)
			{
				return EnumTransparencyState.FADING_IN;
			}
			else
			{
				return EnumTransparencyState.TRANSPARENT;
			}
		}

	}

	private void updateEntityVisibility()
	{
		int cycleVisibility = this.getCycleVisibility();
		if (cycleVisibility >= 85)
		{
			this.setTransparencyState(EnumTransparencyState.OPAQUE);
		}
		else if (cycleVisibility == 84)
		{
			this.setTransparencyState(EnumTransparencyState.TRANSPARENT);
			cycleVisibility = 0;
		}
		else if (cycleVisibility > 50)
		{
			this.setTransparencyState(EnumTransparencyState.FADING_OUT);
			cycleVisibility++;
		}
		else if (cycleVisibility > 15)
		{
			this.setTransparencyState(EnumTransparencyState.OPAQUE);
			cycleVisibility++;
		}
		else if (cycleVisibility > 0)
		{
			this.setTransparencyState(EnumTransparencyState.FADING_IN);
			cycleVisibility++;
		}
		else
		{
			this.setTransparencyState(EnumTransparencyState.TRANSPARENT);
		}
		this.setCycleVisibility(cycleVisibility);

		float opacity = this.getOpacity();
		switch (this.getTransparencyState())
		{
			case TRANSPARENT:
				opacity = 0.0F;
				break;
			case FADING_IN:
				opacity += 0.035F;
				if (opacity > 1.0F) opacity = 1.0F;
				break;
			case OPAQUE:
				opacity = 1.0F;
				break;
			case FADING_OUT:
				opacity -= 0.0275F;
				if (opacity < 0.0F) opacity = 0.0F;
				break;
		}
		this.setOpacity(opacity);
	}


	private float moveAngle, moveSpeed, moveCurve;
	private int entcount, cantMove;
	private EntityMob child;

	public int dronesProduced;

	public void _onLivingUpdate()
	{
		if (!this.world.isRemote)
		{


			// if the haunter doesn't currently have an attack target
			if (this.getAttackTarget() == null)
			{
				this.moveStrafing = 0F;
//				this.despawnEntity();
				this.setMoveForward(0.3F);

				// move on the X and Z axes
				this.motionX = Math.sin(0.01745329F * this.moveAngle) * (double)this.moveSpeed;
				this.motionZ = Math.cos(0.01745329F * this.moveAngle) * (double)this.moveSpeed;

				// if we've hit something...
				if (this.isCollidedHorizontally)
				{
					// and we can't move...
					this.cantMove++;
					if (this.cantMove >= 100)
					{
						// change the direction of travel for the next iteration
						this.moveAngle = this.rand.nextFloat() * 360.0F;
						this.cantMove = 0;
					}
				}
				else this.cantMove = 0;

				// if we're in water...
				if (this.handleWaterMovement())
				{
					// move upwards slowly
					this.motionY = 0.1F;
				}

				// change the angle of travel and the yaw rotation
				this.moveAngle += this.moveCurve;
				this.rotationYaw = -this.moveAngle;

//				// i think entcount is a timer...
//				this.entcount++;
//				if (this.entcount >= 20)
//				{
//					this.entcount = 0;
//
//					// if the child mob is null...
//					if (child == null)
//					{
//						// find a player to attack
//						EntityPlayer player = null; //this.findGuyToAttack(this.active ? 64.0D : 24.0D);
//						if (player != null || 1 == 2)
//						{
//							// create a child monster
//							child = null; //this.make_a_monster(player);
//							if (child != null)
//							{
//								// increment the counter of child mobs we've created
//								this.dronesProduced++;
//							}
//						}
//					}
//					// if the child mob isn't null...
//					else
//					{
//						// if the child mob is dead, or more than 300 ticks old, and the cycleVisibility is zero...
//						int childAge = child.getAge();
//						if ((child.isDead || childAge > 300) && this.getCycleVisibility() == 0)
//						{
//							// play the appear sound, set the cycle visibiliity value to 1, and make the haunter transparent
//							this.playSound(ModSoundEvents.ENTITY_HAUNTER_APPEAR, 5.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F) + 1.0F);
//							this.setCycleVisibility(1);
//							this.setTransparencyState(EnumTransparencyState.TRANSPARENT);
//						}
//					}
//				}
			}

			// if the haunter does currently have an attack target
			else // if (this.getAttackTarget() == null)
			{
				this.setMoveForward(this.onGround ? -0.8F : -0.9F);


				if (this.moveStrafing > 0.1F)
				{
					this.moveStrafing -= 0.075F;
				}
				else if (this.moveStrafing < -0.1F)
				{
					this.moveStrafing += 0.075F;
				}

	//			super.updateEntityActionState();

//				this.entcount++;
//				if (this.entcount >= 4)
//				{
//					this.entcount = 0;
//
//					if (this.getAttackTarget().isDead || this.getAttackTarget().getDistanceToEntity(this) >= 16F)
//					{
//						this.setAttackTarget(null);
//						this.setCycleVisibility(51);
//						this.Angle = this.rand.nextFloat() * 360F;
//					}
//				}
			}

			if (this.motionY < -0.05F)
			{
				this.motionY = -0.05F;
			}

			this.rotationPitch = 0.0F;

		}

		super.onLivingUpdate();

	}

	/**
	 * Overridden to prevent particle effects from Haunters when they land
	 * Combined from {@link EntityLivingBase#updateFallState(double, boolean, IBlockState, BlockPos)} and Combined from {@link Entity#updateFallState(double, boolean, IBlockState, BlockPos)}
	 */
	@Override
	protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos)
	{
		// Copied from EntityLivingBase#updateFallState
		if (!this.isInWater())
		{
			this.handleWaterMovement();
		}
		if (!this.world.isRemote && this.fallDistance > 3.0F && onGround)
		{
			float f = (float)MathHelper.ceil(this.fallDistance - 3.0F);
			if (!state.getBlock().isAir(state, world, pos))
			{
				double d0 = Math.min((double)(0.2F + f / 15.0F), 2.5D);
				int i = (int)(150.0D * d0);
//				if (!state.getBlock().addLandingEffects(state, (WorldServer)this.world, pos, state, this, i))
//				((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] {Block.getStateId(state)});
			}
		}
		// Copied from Entity#updateFallState
		if (onGround)
		{
			if (this.fallDistance > 0.0F)
			{
				state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance);
			}
			this.fallDistance = 0.0F;
		}
		else if (y < 0.0D)
		{
			this.fallDistance = (float)((double)this.fallDistance - y);
		}
	}

}