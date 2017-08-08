package net.crazysnailboy.mods.halloween.entity.monster;

import javax.annotation.Nullable;
import net.crazysnailboy.mods.halloween.init.ModLootTables;
import net.crazysnailboy.mods.halloween.util.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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

	private int jackson;
	private boolean hideWithMe;


	public EntityZombieHands(World world)
	{
		super(world);
		this.setChild(false);
		this.setAIMoveSpeed(0.4F);
		this.setSize(0.6F, 0.8F);
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

//	@Override
//	public boolean getCanSpawnHere()
//	{
//		if (super.getCanSpawnHere())
//		{
//			BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
//			if (this.world.getLight(pos) > 0 && BlockUtils.isSoftGround(this.world, pos.down()))
//			{
//				this.hideWithMe = true;
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			if (this.jackson > 0)
			{
				this.jackson--;
				System.out.println("EntityZombieHands#onLivingUpdate :: { jackson=" + jackson + " }");
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
		super.onLivingUpdate();
	}

	/**
	 * Gives armor or weapon for entity based on given DifficultyInstance
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


	private EntityZombie unearthMe()
	{
		EntityZombie zombie = new EntityZombie(this.world);
		zombie.renderYawOffset = this.renderYawOffset;
		zombie.prevRotationPitch = zombie.rotationPitch = this.rotationPitch;
		zombie.prevRotationYaw = zombie.rotationYaw = this.rotationYaw;
		zombie.setPosition(this.posX, this.posY - 0.75D, this.posZ);
		zombie.setHealth(this.getHealth());
		zombie.setFire(EntityUtils.getFire(this));
		zombie.setAttackTarget(this.getAttackTarget());

		this.world.spawnEntity(zombie);
		this.setDead();
		this.springEffect();
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
		entity.setFire(EntityUtils.getFire(zombie));
		entity.setAttackTarget(zombie.getAttackTarget());

		this.world.spawnEntity(entity);
		zombie.setDead();
		entity.springEffect();
		return entity;
	}


	private void springEffect()
	{
		BlockPos pos = this.getPosition(); // BlockPos pos = new BlockPos(Math.floor(this.posX), Math.floor(this.getEntityBoundingBox().minY), Math.floor(this.posZ));
		IBlockState state = this.world.getBlockState(pos.down());

		if (!(state.getBlock() instanceof BlockAir))
		{
			SoundType soundType = state.getBlock().getSoundType(null, null, null, null);
			this.playSound(soundType.getStepSound(), soundType.getVolume(), soundType.getPitch());

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