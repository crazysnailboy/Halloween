package net.crazysnailboy.mods.halloween.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;


public class EntityCreeperCurse extends EntityCurse
{

	public EntityCreeperCurse(World world)
	{
		this(world, null);
	}

	public EntityCreeperCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.lifetime > 0 && this.victim != null && this.victim instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)this.victim;
			if (player.isSwingInProgress)
			{
				this.swell += 2;
				if (this.swell == 2)
				{
					this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
				}
				else if (this.swell >= 28)
				{
					this.world.createExplosion(this, this.posX, this.posY - 1.0D, this.posZ, 3.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.setDead();
				}
			}
		}
	}

	@Override
	protected void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_CREEPER_HURT, 0.75F, (this.rand.nextFloat() * 0.4F) + 0.8F); // "mob.creeper"
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.CREEPER;
	}

}
