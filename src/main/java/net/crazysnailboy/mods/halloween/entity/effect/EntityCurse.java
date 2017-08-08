package net.crazysnailboy.mods.halloween.entity.effect;

import java.util.List;
import java.util.Random;
import java.util.UUID;
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
	protected int lifetime;
	protected int dash, swell, lift;
	private float orbital;


	public EntityCurse(World world)
	{
		this(world, null);
	}

	public EntityCurse(World world, EntityLivingBase victim)
	{
		super(world);

		this.lifetime = 1200;
		this.orbital = this.rand.nextFloat() * 360.0F;
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
				((EntityPlayer)this.victim).sendMessage(new TextComponentTranslation("chat.curse.struck", this.getCurseType().getDisplayName()));
			}
			this.setPosition(this.victim.posX, this.victim.posY + this.victim.getEyeHeight(), this.victim.posZ);
		}
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();

		// if this is a newly spawned curse (lifetime == 1200) with a victim...
		if (this.lifetime == 1200 && this.victim != null)
		{
			// search for other curses in the vicinity of this curse...
			List<EntityCurse> entities = this.world.getEntitiesWithinAABB(EntityCurse.class, this.getEntityBoundingBox().expand(4.0D, 4.0D, 4.0D));
			for ( EntityCurse entity : entities )
			{
				if (entity != this && entity.victim == this.victim)
				{
					// ... and kill them
					entity.victim = null;
					entity.setDead();
				}
			}
		}

		// if this curse doesn't have a victim...
		if (this.victim == null)
		{
			// search for players in the vicinity
			EntityPlayer player = this.world.getClosestPlayerToEntity(this, 2.0D);

			// if we've found one, it will become the victim of this curse
			if (player != null && !player.isDead && player.getHealth() > 0.0F)
			{
				this.victim = player;
			}
			// otherwise, kill this curse
			else
			{
				this.setDead();
			}
		}

		// if this curse isn't new, and has a victim...
		else
		{
			// if the victim is dead, kill this curse
			if (this.victim.getHealth() <= 0 || this.victim.isDead)
			{
				this.setDead();
			}
			// otherwise
			else
			{
				// ensure this curse continues to rotate around it's victim as it's victim moves
				this.posX = this.victim.posX;
				if (this.victim.prevPosY != this.victim.posY) this.posY = this.victim.posY + this.victim.getEyeHeight();
				this.posZ = this.victim.posZ;
				this.setPosition(this.posX, this.posY, this.posZ);

				// every 20 ticks, perform the curse effect
				if ((this.lifetime - 1) % 20 == 0)
				{
					this.performCurse();
				}
			}
		}

		// orbital, dash, lift and swell are used for rendering
		this.orbital += 6.0F;
		if (this.orbital > 360.0F) this.orbital -= 360.0F;

		if (this.dash > 0) this.dash--;
		if (this.swell > 0) this.swell--;
		if (this.lift > 0) this.lift--;

		// decrement the lifetime counter, and kill this curse when the counter gets to zero
		this.lifetime--;
		if (this.lifetime <= 0) this.setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.lifetime = compound.getShort("Lifetime");

		String s = compound.getString("VictimUUID");
		if (!s.isEmpty())
		{
			this.victim = this.world.getPlayerEntityByUUID(UUID.fromString(s));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setShort("Lifetime", (short)this.lifetime);

		if (this.victim != null)
		{
			compound.setString("VictimUUID", this.victim.getUniqueID().toString());
		}
	}

	/**
	 * empty concrete implementation of {@link net.minecraft.entity.Entity#entityInit()}
	 */
	@Override
	protected void entityInit()
	{
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
		CREEPER("creeper", "§6" + "Creeper"),
		GHAST("ghast", "§a" + "Ghast"),
		SKELETON("skeleton", "§b" + "Skeleton"),
		SLIME("slime", "§c" + "Slime"),
		SPIDER("spider", "§d" + "Spider"),
		ZOMBIE("zombie", "§e" + "Zombie");

		private final String curseName;
		private final String dislayName;


		private EnumCurseType(String curseName, String displayName)
		{
			this.curseName = curseName;
			this.dislayName = displayName;
		}


		public String getName()
		{
			return this.curseName;
		}

		public String getDisplayName()
		{
			return this.dislayName;
		}


		public static EnumCurseType getRandom()
		{
			return values()[new Random().nextInt(values().length)];
		}

		public static EnumCurseType byName(String curseName)
		{
			for ( EnumCurseType value : values())
			{
				if (value.curseName.equals(curseName))
				{
					return value;
				}
			}
			return null;
		}

	}

}