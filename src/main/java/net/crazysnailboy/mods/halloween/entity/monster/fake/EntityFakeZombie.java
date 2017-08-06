package net.crazysnailboy.mods.halloween.entity.monster.fake;

import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;


/**
 * A "fake" zombie. Created by {@link EntityHaunter} during it's attack.
 *
 */
public class EntityFakeZombie extends EntityZombie implements IFakeMonster
{

	private int suspension;


	public EntityFakeZombie(World world)
	{
		super(world);
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();
		suspension++;
		if (suspension > 300) attackEntityFrom(DamageSource.causeMobDamage(this), 100.0F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		Entity entity = source.getTrueSource();
		boolean takenDamage = super.attackEntityFrom(source, amount);

		if (takenDamage && entity != null)
		{
			this.playSound(ModSoundEvents.ENTITY_FAKE_FADE, 1.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.1F) + 1.0F);
			this.spawnExplosionParticle();
			this.spawnExplosionParticle(); // TODO - examine why this is being fired twice - twice as many particles?
			this.setDead();
		}

		return takenDamage;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_FAKE_FADE;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setShort("Suspension", (short)suspension);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		suspension = compound.getShort("Suspension");
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return null;
	}


	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

}
