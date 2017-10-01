package net.crazysnailboy.mods.halloween.entity.monster;

import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.crazysnailboy.mods.halloween.entity.ai.EntityAICreeperween;
import net.crazysnailboy.mods.halloween.init.ModItems;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.item.ItemCandy.EnumCandyFlavour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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
		this.setEntityInvulnerable(ModConfiguration.invulnerableCreeperweens);
	}


	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(STATE, Integer.valueOf(-1));
		this.dataManager.register(IGNITED, Boolean.valueOf(false));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAICreeperween.Swell(this));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
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
	protected SoundEvent getHurtSound()
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
		return (ModConfiguration.invulnerableCreeperweens ? null : ModLootTables.ENTITIES_HALLOWMOB);
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

				// *** modified from InventoryHelper.spawnItemStack ***
				float randX = this.rand.nextFloat() * 0.8F + 0.3F;
				float randY = this.rand.nextFloat() * 0.8F + 0.3F;
				float randZ = this.rand.nextFloat() * 0.8F + 0.3F;

				while (!stack.isEmpty())
				{
					EntityItem entityitem = new EntityItem(this.world, this.posX + (double)randX, this.posY + (double)randY, this.posZ + (double)randZ, stack.splitStack(this.rand.nextInt(21) + 10));
					entityitem.motionX = this.rand.nextGaussian() * 0.1D;
					entityitem.motionY = this.rand.nextGaussian() * 0.1D + 0.4D;
					entityitem.motionZ = this.rand.nextGaussian() * 0.1D;
					this.world.spawnEntity(entityitem);
				}
				// *** modified from InventoryHelper.spawnItemStack ***
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

	@Override
	public boolean getCanSpawnHere()
	{
		return ModConfiguration.isHalloween && ModConfiguration.enableCreeperweens && super.getCanSpawnHere();
	}

}