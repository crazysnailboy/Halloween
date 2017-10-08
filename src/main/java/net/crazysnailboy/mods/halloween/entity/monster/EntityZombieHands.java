package net.crazysnailboy.mods.halloween.entity.monster;

import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.util.BlockUtils;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


public class EntityZombieHands extends EntityZombie
{

	private static final Field entityFire = ReflectionUtils.getDeclaredField(Entity.class, "fire", "field_70151_c");

	private int jackson, entCount;
	private boolean hideWithMe;


	public EntityZombieHands(World world)
	{
		super(world);
		this.setChild(false);
		this.setAIMoveSpeed(0.4F);
		this.setSize(0.6F, 0.8F);
	}


	/**
	 * Overridden to call {@link EntityZombie#setChild(boolean)} to force all ZombieHands to be adults
	 */
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		super.onInitialSpawn(difficulty, livingdata);
		this.setChild(false);
		return livingdata;
	}


	@Override
	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			if (this.jackson > 0)
			{
				this.jackson--;
			}

			if (!this.isDead && this.getHealth() > 0)
			{
				if (!this.onGround)
				{
					this.jackson += 2;
					if (this.jackson >= 5)
					{
						EntityZombie zombie = this.unearthMe();
						zombie.motionY = 0.0D;
					}
				}
				else if (this.getAttackTarget() != null)
				{
					for (int i = 0; i < 9; i++)
					{
						double g = (double)((i / 3) - 1) * 0.125D;
						double h = (double)((i % 3) - 1) * 0.125D;

						BlockPos pos = new BlockPos(Math.floor(this.posX + g), Math.floor(this.getEntityBoundingBox().minY - 1.0D), Math.floor(this.posZ + h));
						IBlockState state = this.world.getBlockState(pos);

						if (state.getBlock().isPassable(this.world, pos))
						{
							this.jackson += 2;
							if (this.jackson >= 4)
							{
								EntityZombie zombie = this.unearthMe();
								zombie.motionY = 0.5D;
							}
							break;
						}
						else if (i == 4)
						{
							Material material = state.getMaterial();
							if (!material.isOpaque())
							{
								this.jackson += 2;
								if (this.jackson >= 4)
								{
									EntityZombie zombie = this.unearthMe();
									zombie.motionY = 0.5D;
								}
								break;
							}
						}
					}
				}
			}
		}


		if (hideWithMe && this.getAttackTarget() == null)
		{
			entCount++;
			if (entCount >= 64)
			{
				entCount = rand.nextInt(8);

				List<EntityZombie> entities = this.world.getEntitiesWithinAABB(EntityZombie.class, this.getEntityBoundingBox().expand(16.0D, 16.0D, 16.0D), new Predicate<EntityZombie>()
				{
					@Override
					public boolean apply(@Nullable EntityZombie entity)
					{
						return (entity != EntityZombieHands.this && !(entity instanceof EntityZombieHands));
					}
				});

				for ( EntityZombie entity : entities )
				{
					if (entity.getAttackTarget() == null && entity.onGround && entity.getRidingEntity() == null && !entity.isBeingRidden() && !entity.handleWaterMovement())
					{
						if (this.world.getLight(entity.getPosition()) != 0 && BlockUtils.isSoftGround(world, entity.getPosition().down()))
						{
							entity = earthMe(entity);
						}
					}
				}
			}
		}


		super.onLivingUpdate();
	}

	/**
	 * Overriden so that ZombieHands don't spawn with equipment
	 */
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("HideWithMe", this.hideWithMe);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.hideWithMe = compound.getBoolean("HideWithMe");
		this.setChild(false);
	}

	@Override
	public int getMaxSpawnedInChunk()
	{
		return 6;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return null;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLootTables.ENTITIES_HALLOWMOB;
	}

//	@Override
//	public void jump()
//	{
//		if (this.getAttackTarget() != null && this.isCollidedHorizontally)
//		{
//			this.jackson += 2;
//			if (this.jackson >= 25)
//			{
//				EntityZombie zombie = this.unearthMe();
//				zombie.motionY = 0.5D;
//			}
//		}
//	}

	@Override
	public boolean getCanSpawnHere()
	{
		if (ModConfiguration.isHalloween && ModConfiguration.enableZombieHands && super.getCanSpawnHere())
		{
			BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
			if (this.world.getLight(pos) > 0 && BlockUtils.isSoftGround(this.world, pos.down()))
			{
				this.hideWithMe = true;
				return true;
			}
		}
		return false;
	}


	private EntityZombie unearthMe()
	{
		EntityZombie zombie = new EntityZombie(this.world);
		zombie.renderYawOffset = this.renderYawOffset;
		zombie.prevRotationPitch = zombie.rotationPitch = this.rotationPitch;
		zombie.prevRotationYaw = zombie.rotationYaw = this.rotationYaw;
		zombie.setPosition(this.posX, this.posY, this.posZ);
		zombie.setHealth(this.getHealth());
		zombie.setFire((Integer)ReflectionUtils.getFieldValue(entityFire, this));
		zombie.setZombieType(this.getZombieType());
		zombie.setAttackTarget(this.getAttackTarget());

		this.springEffect();
		this.setDead();

		this.world.spawnEntity(zombie);
		return zombie;
	}

	private EntityZombieHands earthMe(EntityZombie zombie)
	{
		EntityZombieHands entity = new EntityZombieHands(this.world);
		entity.renderYawOffset = zombie.renderYawOffset;
		entity.prevRotationPitch = entity.rotationPitch = zombie.rotationPitch;
		entity.prevRotationYaw = entity.rotationYaw = zombie.rotationYaw;
		entity.setPosition(zombie.posX, zombie.posY, zombie.posZ);
		entity.setHealth(zombie.getHealth());
		entity.setFire((Integer)ReflectionUtils.getFieldValue(entityFire, zombie));
		entity.setZombieType(this.getZombieType());
		entity.setAttackTarget(zombie.getAttackTarget());

		zombie.setDead();
		entity.springEffect();

		this.world.spawnEntity(entity);
		return entity;
	}


	private void springEffect()
	{
		BlockPos pos = this.getPosition();
		IBlockState state = this.world.getBlockState(pos.down());

		if (!(state.getBlock() instanceof BlockAir))
		{
			try
			{
				SoundType soundType = state.getBlock().getSoundType(state, this.world, pos, this);
				this.playSound(soundType.getStepSound(), soundType.getVolume(), soundType.getPitch());
			}
			catch(Exception ex)
			{
			}

			for (int q = 0; q < 48; q++)
			{
				double pft = (this.rand.nextFloat() - 0.5F) * 0.75F;
				double qft = (this.rand.nextFloat() - 0.5F) * 0.75F;

				this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + pft, this.posY + 0.5D, this.posZ + qft, 0.0D, 0.25D, 0.0D, new int[] { Block.getStateId(state) });

//				EntityDiggingFX gordon = new EntityDiggingFX(world, posX + pft, posY + 0.5D, posZ + qft, 0.0D, 0.25D, 0.0D, Block.blocksList[x], 0, y);
//				gordon.motionY += 0.15D + (rand.nextFloat() * 0.1D);
//				ModLoader.getMinecraftInstance().effectRenderer.addEffect(gordon);
			}
		}
	}

}