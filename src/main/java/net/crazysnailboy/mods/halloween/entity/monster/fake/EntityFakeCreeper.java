package net.crazysnailboy.mods.halloween.entity.monster.fake;

import java.lang.reflect.Field;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;


/**
 * A "fake" {@link EntityCreeper}. Created by {@link EntityHaunter} during it's attack.
 *
 */
public class EntityFakeCreeper extends EntityCreeper implements IFakeMonster
{

	private static final Field explosionRadius = ReflectionUtils.getDeclaredField(EntityCreeper.class, "explosionRadius", "field_82226_g");

	private int suspension;


	public EntityFakeCreeper(World world)
	{
		super(world);
		this.setExplosionRadius((byte)0);
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
		Entity entity = source.getEntity();
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
		this.setExplosionRadius((byte)0);
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return null;
	}


	/**
	 * Uses reflection to set the value of private field {@link EntityCreeper.explosionRadius}
	 * so that the fake creeper causes no damage when it explodes.
	 */
	private void setExplosionRadius(byte value)
	{
		ReflectionUtils.setFieldValue(explosionRadius, this, value);
	}

}