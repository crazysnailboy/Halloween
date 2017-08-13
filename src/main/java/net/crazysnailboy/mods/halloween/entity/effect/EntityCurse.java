package net.crazysnailboy.mods.halloween.entity.effect;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.crazysnailboy.mods.halloween.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


/**
 * Revival Mode
 *   Creeper Curse: If you swing your weapon too often, the creepers circling around your head will explode.
 *   Ghast Curse:  Monsters within 50 blocks of you will gradually walk towards your position.
 *   Skeleton Curse: Your health will be in a constant state of flux.
 *   Slime Curse: You will be slowed down, and your jump height will be increased.
 *   Spider Curse: Your character will constantly be bumped around the screen.
 *   Zombie Curse: Your inventory will be constantly flooded with poisionous potatoes.
 *
 * Legacy Mode:
 *   Creeper Curse: If you swing your weapon too often, the creepers circling around your head will explode.
 *   Ghast Curse:  Monsters within 50 blocks of you will gradually walk towards your position.
 *   Skeleton Curse: Your health will be in a constant state of flux.
 *   Slime Curse: Your inventory will be constantly flooded with slime balls.
 *   Spider Curse: Your character will constantly be bumped around the screen.
 *   Zombie Curse: You will be slowed down, and your jump height will be decreased.
 *
 */
public abstract class EntityCurse extends Entity
{

	private static final DataParameter<Integer> VICTIM_ID = EntityDataManager.<Integer>createKey(EntityCurse.class, DataSerializers.VARINT);

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
			if (!this.world.isRemote)
			{
				this.dataManager.set(VICTIM_ID, (this.victim == null ? 0 : this.victim.getEntityId()));
			}
			if (this.victim instanceof EntityPlayer)
			{
				((EntityPlayer)this.victim).sendMessage(new TextComponentTranslation("chat.curse.struck", this.getCurseType().getDisplayName()));
			}
			this.setPosition(this.victim.posX, this.victim.posY + this.victim.getEyeHeight(), this.victim.posZ);
		}
	}


	@Override
	protected void entityInit()
	{
		this.dataManager.register(VICTIM_ID, 0);
	}

	@Override
	public void onUpdate()
	{
		if (!this.world.isRemote)
		{
			// if this curse has a victim...
			if (this.victim != null)
			{
				if (this.firstUpdate)
				{
					// search for other curses in the vicinity of this curse
					List<EntityCurse> entities = this.world.getEntitiesWithinAABB(EntityCurse.class, this.getEntityBoundingBox().expand(4.0D, 4.0D, 4.0D));
					for ( EntityCurse entity : entities )
					{
						// if we've found other curses with the same victim as this curse...
						if (entity != this && entity.victim == this.victim)
						{
							// ... kill the other curses
							entity.victim = null;
							entity.setDead();
						}
					}
				}

				// if our victim is dead
				if (this.victim.getHealth() <= 0 || this.victim.isDead)
				{
					// kill this curse
					this.setDead();
				}
			}

			// if this curse doesn't have a victim...
			if (this.victim == null)
			{
				// get the closest living entity in the vicinity
				EntityLivingBase entity = WorldUtils.getClosestEntity(this.world, EntityLivingBase.class, this.getPosition(), 2.0D);

				// if we've found one...
				if (entity != null && !entity.isDead && entity.getHealth() > 0.0F)
				{
					// ... make it the victim of this curse
					this.victim = entity;
				}
				// otherwise...
				else
				{
					// ... kill this curse
					this.setDead();
				}
			}
		}

		// if the loop is running client-side and we don't yet have a victim...
		if (this.world.isRemote && this.victim == null)
		{
			// attempt to get the victim by id from the data manager
			this.victim = (EntityLivingBase)this.world.getEntityByID(this.dataManager.get(VICTIM_ID).intValue());
		}

		// if this curse has a victim...
		if (this.victim != null)
		{
			// ensure this curse continues to rotate around it's victim as it's victim moves
			this.posX = this.victim.posX;
			this.posY = this.victim.posY + this.victim.getEyeHeight();
			this.posZ = this.victim.posZ;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.motionX = this.victim.motionX;
			this.motionY = this.victim.motionY;
			this.motionZ = this.victim.motionZ;

			// every 20 ticks, perform the curse effect
			if ((this.lifetime - 1) % 20 == 0)
			{
				this.performCurse();
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

		super.onUpdate();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.lifetime = compound.getShort("Lifetime");
		String s = compound.getString("VictimUUID");
		if (!s.isEmpty())
		{
			this.victim = WorldUtils.getEntityByUUID(this.world, EntityLivingBase.class, UUID.fromString(s));
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

	@Override
	public void setDead()
	{
		this.victim = null;
		super.setDead();
	}


	public void performCurse()
	{
		this.dash = 8;
		this.swell = 8;
		this.lift = 2;
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