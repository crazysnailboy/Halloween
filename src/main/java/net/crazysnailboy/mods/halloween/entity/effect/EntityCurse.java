package net.crazysnailboy.mods.halloween.entity.effect;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


public abstract class EntityCurse extends Entity
{

	public EntityLivingBase victim;
	public float orbital;
	public int lifetime, dash, swell, lift;
	public boolean gotVictim;


	public EntityCurse(World world)
	{
		this(world, null);
	}

	public EntityCurse(World world, EntityLivingBase victim)
	{
		super(world);

		this.lifetime = 1200;
		this.orbital = this.rand.nextFloat() * 360F;
		this.noClip = true;
		this.setSize(0.25F, 0.25F);

		this.victim = victim;
		if (this.victim == null)
		{
			this.setPosition(this.posX, this.posY, this.posZ);
		}
		else
		{
			if (this.victim instanceof EntityPlayer)
			{
				((EntityPlayer)this.victim).sendMessage(new TextComponentTranslation("chat.curse.struck", this.getCurseType().getName()));
			}
			this.setPosition(this.victim.posX, this.victim.posY, this.victim.posZ);
		}
	}


	@Override
	protected void entityInit()
	{
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		this.orbital += 6D;
		if (this.orbital > 360D) this.orbital -= 360D;

		if (this.lifetime == 1200 && this.victim != null)
		{
			List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(4D, 4D, 4D));
			for (Entity entity : entities)
			{
				if (entity instanceof EntityCurse)
				{
					EntityCurse curse = (EntityCurse)entity;
					if (curse.victim == this.victim)
					{
						curse.victim = null;
						curse.setDead();
					}
				}
			}
		}

		if (this.victim == null)
		{
			this.victim = null;
			if (this.gotVictim)
			{
				this.gotVictim = false;
				List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.5D, 2D, 0.5D));
				for (Entity entity : entities)
				{
					if (entity instanceof EntityLivingBase)
					{
						EntityLivingBase entityLiving = (EntityLivingBase)entity;
						if (!entityLiving.isDead && entityLiving.getHealth() > 0)
						{
							this.victim = entityLiving;
							break;
						}
					}
				}
			}
			else
			{
				this.setDead();
			}
		}
		else
		{
			if (this.victim.getHealth() <= 0 || this.victim.isDead)
			{
				this.setDead();
			}
			else
			{
				this.posX = (this.victim.getEntityBoundingBox().minX + this.victim.getEntityBoundingBox().maxX) / 2D;
				this.posY = this.victim.getEntityBoundingBox().minY + (this.victim.height * 0.875F);
				this.posZ = (this.victim.getEntityBoundingBox().minZ + this.victim.getEntityBoundingBox().maxZ) / 2D;
				this.setPosition(this.posX, this.posY, this.posZ);
				if ((this.lifetime - 1) % 20 == 0)
				{
					this.performCurse();
				}
			}
		}

		this.lifetime--;

		if (this.dash > 0)
		{
			this.dash--;
		}

		if (this.swell > 0)
		{
			this.swell--;
		}

		if (this.lift > 0)
		{
			this.lift--;
		}

		if (this.lifetime <= 0)
		{
			this.setDead();
		}
	}



	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
        this.lifetime = compound.getShort("Lifetime");
		this.gotVictim = compound.getBoolean("GotVictim");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setShort("Lifetime", (short)this.lifetime);
		this.gotVictim = (this.victim != null);
		compound.setBoolean("GotVictim", this.gotVictim);
	}


	public void performCurse()
	{
		this.doTickSound();
	}

	protected void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_GENERIC_HURT, 1.0F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}


	public float getRotation()
	{
		return this.orbital;
	}

	public float getDash()
	{
		return ((float)this.dash / 24.0F);
	}

	public float getSwell()
	{
		return ((float)this.swell / 50.0F);
	}

	public float getLift()
	{
		return ((float)this.lift / 32.0F);
	}

	public abstract EnumCurseType getCurseType();


	public enum EnumCurseType
	{
		CREEPER("§6" + "Creeper"),
		GHAST("§a" + "Ghast"),
		SKELETON("§b" + "Skeleton"),
		SLIME("§c" + "Slime"),
		SPIDER("§d" + "Spider"),
		ZOMBIE("§e" + "Zombie");

		private final String curseName;

		private EnumCurseType(String curseName)
		{
			this.curseName = curseName;
		}

		public String getName()
		{
			return this.curseName;
		}

		public static EnumCurseType getRandom()
		{
			return values()[new Random().nextInt(values().length)];
		}

	}

}
