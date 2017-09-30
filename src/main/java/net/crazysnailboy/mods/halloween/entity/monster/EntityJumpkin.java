package net.crazysnailboy.mods.halloween.entity.monster;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.crazysnailboy.mods.halloween.entity.ai.EntityAIJumpkin;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.util.EntityUtils;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


public class EntityJumpkin extends EntitySlime
{

	private static final DataParameter<Boolean> LIT = EntityDataManager.<Boolean>createKey(EntityJumpkin.class, DataSerializers.BOOLEAN);

	private boolean awakened;


	public EntityJumpkin(World world)
	{
		super(world);
		this.isImmuneToFire = true;
		this.setSlimeSize(2);
		this.alignToBlocks();
	}


	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(LIT, false);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.awakened ? 16.0D : 4.0D);
	}

	/**
	 * Overridden to call {@link EntitySlime#setSlimeSize(int)} to force all Jumpkins to size 2.
	 */
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		super.onInitialSpawn(difficulty, livingdata);
		this.setSlimeSize(2);
		this.alignToBlocks();
		return livingdata;
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		this.removeUnneededAITasks();
		this.tasks.addTask(3, new EntityAIJumpkin.FaceRandom(this));
		this.tasks.addTask(5, new EntityAIJumpkin.Hop(this));
	}

	@Override
	public void onLivingUpdate()
	{
		// ensure that jumpkins spawn aligned as blocks (TODO - doesn't work)
		if (this.firstUpdate)
		{
			this.alignToBlocks();
		}

		if (!this.world.isRemote)
		{
			// if it's daytime, jumpkins have a chance to turn into pumpkins
			if (this.world.isDaytime())
			{
				float brightness = this.getBrightness(1.0F);
				if (brightness > 0.5F && this.world.canBlockSeeSky(this.getPosition()) && ((this.rand.nextFloat() * 30F) < ((brightness - 0.4F) * 2.0F)))
				{
					if (this.onGround || this.isInWater())
					{
						IBlockState state = Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, this.getHorizontalFacing());
						this.world.setBlockState(this.getPosition(), state);
					}
					this.setDead();
					this.spawnExplosionParticle();
				}
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("Awakened", this.awakened);
		compound.setBoolean("Lit", this.getLit());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setLit(compound.getBoolean("Lit"));
		this.setAwakened(compound.getBoolean("Awakened"));
		this.setSlimeSize(2);
	}

	@Override
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.FLAME;
	}

	@Override
	protected SoundEvent getHurtSound()
	{
		return SoundEvents.BLOCK_WOOD_STEP;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.BLOCK_WOOD_STEP;
	}

	@Override
	protected SoundEvent getSquishSound()
	{
		return SoundEvents.BLOCK_WOOD_FALL;
	}

	@Override
	protected SoundEvent getJumpSound()
	{
		return SoundEvents.BLOCK_WOOD_STEP;
	}

	@Override
	protected void setSlimeSize(int size)
	{
		super.setSlimeSize(size);
		this.setSize(0.5F * (float)size, 0.5F * (float)size);
	}

	/**
	 * Overridden to prevent Jumpkins from dropping Slime Balls
	 */
	@Override
	protected Item getDropItem()
	{
		return null;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLootTables.ENTITIES_HALLOWMOB;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		return this.world.getBlockState(this.getPosition().down()).getMaterial() == Material.GRASS && EntityUtils.getCanMobSpawnHere(this);
	}

	/**
	 * Overridden to prevent Jumpkins spawning smaller Jumpkins when they die
	 */
	@Override
	public void setDead()
	{
		this.isDead = true;
	}

	@Override
	public int getMaxSpawnedInChunk()
	{
		return 16;
	}


	public boolean getLit()
	{
		return this.dataManager.get(LIT).booleanValue();
	}

	public void setLit(boolean value)
	{
		this.dataManager.set(LIT, value);
	}

	public boolean getAwakened()
	{
		return this.awakened;
	}

	public void setAwakened(boolean value)
	{
		this.awakened = value;
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.awakened ? 16.0D : 4.0D);
	}


	private void removeUnneededAITasks()
	{
		// remove the EntitySlime$AISlimeHop and EntitySlime$AISlimeFaceRandom tasks, as we're going to replace those with custom ones
		final Class AISlimeHop = ReflectionUtils.getClass("net.minecraft.entity.monster.EntitySlime$AISlimeHop");
		final Class AISlimeFaceRandom = ReflectionUtils.getClass("net.minecraft.entity.monster.EntitySlime$AISlimeFaceRandom");

		Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.tasks.taskEntries.iterator();
		while (iterator.hasNext())
		{
			EntityAITasks.EntityAITaskEntry taskEntry = iterator.next();
			if (AISlimeHop.isInstance(taskEntry.action) || AISlimeFaceRandom.isInstance(taskEntry.action))
			{
				iterator.remove();
			}
		}

		// remove the EntityAIFindEntityNearest target task, as we don't want jumpkins to target golems.
		iterator = this.targetTasks.taskEntries.iterator();
		while (iterator.hasNext())
		{
			EntityAITasks.EntityAITaskEntry taskEntry = iterator.next();
			if (taskEntry.action instanceof EntityAIFindEntityNearest)
			{
				iterator.remove();
			}
		}
	}

	private void alignToBlocks()
	{
		this.rotationPitch = 0.0F;
		this.rotationYaw = this.prevRotationYaw = this.rotationYawHead = this.renderYawOffset = (Math.round(this.rotationYaw / 90.0F) * 90.0F);
		this.setRotation(this.rotationYaw, this.rotationPitch);
		this.posX = Math.floor(this.posX) + 0.5D;
		this.posZ = Math.floor(this.posZ) + 0.5D;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.setMoveForward(0.0F);
	}

}