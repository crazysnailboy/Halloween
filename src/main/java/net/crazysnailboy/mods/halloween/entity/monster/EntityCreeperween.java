package net.crazysnailboy.mods.halloween.entity.monster;

import net.crazysnailboy.mods.halloween.init.ModItems;
import net.crazysnailboy.mods.halloween.item.ItemCandy.EnumCandyFlavour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Mostly copied from {@link net.minecraft.entity.monster.EntityCreeper}.
 * Copied rather than extended to avoid having to use access transformers on the private explode method,
 * and to avoid interactions with Ocelots, dropping of Skulls and Music Discs, and other creeper-specific functionality.
 *
 */
public class EntityCreeperween extends EntityMob
{

	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityCreeper.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> IGNITED = EntityDataManager.<Boolean>createKey(EntityCreeper.class, DataSerializers.BOOLEAN);

	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;
	private int explosionRadius = 3;


	public EntityCreeperween(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 2.7F);
		this.setEntityInvulnerable(true);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityCreeperween.EntityAICreeperweenSwell(this));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	public int getMaxFallHeight()
	{
		return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		super.fall(distance, damageMultiplier);
		this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + distance * 1.5F);

		if (this.timeSinceIgnited > this.fuseTime - 5)
		{
			this.timeSinceIgnited = this.fuseTime - 5;
		}
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(STATE, Integer.valueOf(-1));
		this.dataManager.register(IGNITED, Boolean.valueOf(false));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setShort("Fuse", (short)this.fuseTime);
		compound.setByte("ExplosionRadius", (byte)this.explosionRadius);
		compound.setBoolean("ignited", this.hasIgnited());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if (compound.hasKey("Fuse", NBT.TAG_ANY_NUMERIC)) this.fuseTime = compound.getShort("Fuse");
		if (compound.hasKey("ExplosionRadius", NBT.TAG_ANY_NUMERIC)) this.explosionRadius = compound.getByte("ExplosionRadius");
		if (compound.getBoolean("ignited")) this.ignite();
		this.setEntityInvulnerable(true);
	}

	@Override
	public void onUpdate()
	{
		if (this.isEntityAlive())
		{
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.hasIgnited())
			{
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0)
			{
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0)
			{
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime)
			{
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
		}

		super.onUpdate();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_CREEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_CREEPER_DEATH;
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public float getCreeperFlashIntensity(float p_70831_1_)
	{
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (float)(this.fuseTime - 2);
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public int getMaxSpawnedInChunk()
	{
		return 2;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL)
		{
			this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			player.swingArm(hand);

			if (!this.world.isRemote)
			{
				this.ignite();
				stack.damageItem(1, player);
				return true;
			}
		}
		return super.processInteract(player, hand);
	}

	public int getCreeperState()
	{
		return this.dataManager.get(STATE).intValue();
	}

	public void setCreeperState(int state)
	{
		this.dataManager.set(STATE, state);
	}

	/**
	 * Modified from {@link net.minecraft.entity.monster.EntityCreeper#explode} to drop candy when a creeperween explodes.
	 */
	private void explode()
	{
		if (!this.world.isRemote)
		{
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			this.dead = true;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, flag);
			this.setDead();

			// candySplosion
			int count = this.rand.nextInt(8) + 8;
			for (int i = 0; i < count; i++)
			{
				ItemStack stack = new ItemStack(ModItems.CANDY, 1, EnumCandyFlavour.getRandom().getMetadata());
				InventoryHelper.spawnItemStack(this.world, this.posX, this.posY, this.posZ, stack);
			}
		}
	}

	public boolean hasIgnited()
	{
		return ((Boolean)this.dataManager.get(IGNITED)).booleanValue();
	}

	public void ignite()
	{
		this.dataManager.set(IGNITED, Boolean.valueOf(true));
	}


//	@Override
//	public boolean getCanSpawnHere()
//	{
//		if (super.getCanSpawnHere())
//		{
//			BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
//			if (this.world.getLight(pos) > 0 && BlockUtils.isSoftGround(this.world, pos.down()))
//			{
//				return true;
//			}
//		}
//		return false;
//	}


	/**
	 * Copied from {@link net.minecraft.entity.ai.EntityAICreeperSwell}.
	 * Changed to swell an instance of EntityCreeperween rather than EntityCreeper.
	 *
	 */
	static class EntityAICreeperweenSwell extends EntityAIBase
	{

		EntityCreeperween swellingCreeper;
		EntityLivingBase creeperAttackTarget;

		public EntityAICreeperweenSwell(EntityCreeperween entitycreeperIn)
		{
			this.swellingCreeper = entitycreeperIn;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute()
		{
			EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
			return this.swellingCreeper.getCreeperState() > 0 || entitylivingbase != null && this.swellingCreeper.getDistanceSqToEntity(entitylivingbase) < 9.0D;
		}

		@Override
		public void startExecuting()
		{
			this.swellingCreeper.getNavigator().clearPathEntity();
			this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
		}

		@Override
		public void resetTask()
		{
			this.creeperAttackTarget = null;
		}

		@Override
		public void updateTask()
		{
			if (this.creeperAttackTarget == null)
			{
				this.swellingCreeper.setCreeperState(-1);
			}
			else if (this.swellingCreeper.getDistanceSqToEntity(this.creeperAttackTarget) > 49.0D)
			{
				this.swellingCreeper.setCreeperState(-1);
			}
			else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget))
			{
				this.swellingCreeper.setCreeperState(-1);
			}
			else
			{
				this.swellingCreeper.setCreeperState(1);
			}
		}
	}


}
