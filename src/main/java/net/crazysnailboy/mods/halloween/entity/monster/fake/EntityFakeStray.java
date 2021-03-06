package net.crazysnailboy.mods.halloween.entity.monster.fake;

import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeTippedArrow;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;


/**
 * A "fake" {@link EntityStray}. Created by {@link EntityHaunter} during it's attack.
 *
 */
public class EntityFakeStray extends EntityStray implements IFakeMonster
{

	private int suspension;


	public EntityFakeStray(World world)
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
	protected EntityArrow getArrow(float distanceFactor)
	{
		EntityFakeTippedArrow entitytippedarrow = new EntityFakeTippedArrow(this.world, this);
		entitytippedarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
		return entitytippedarrow;
	}

}