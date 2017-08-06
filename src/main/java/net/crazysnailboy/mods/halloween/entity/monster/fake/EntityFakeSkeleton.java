package net.crazysnailboy.mods.halloween.entity.monster.fake;

import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeArrow;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


/**
 * A "fake" zombie. Created by {@link EntityHaunter} during it's attack.
 *
 */
public class EntityFakeSkeleton extends EntitySkeleton implements IFakeMonster
{

	private int suspension;


	public EntityFakeSkeleton(World world)
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


	/**
	 * Overridden to fire "fake" arrows instead of real ones.
	 * see {@link net.minecraft.entity.monster.EntitySkeleton#attackEntityWithRangedAttack(net.minecraft.entity.EntityLivingBase, float)}.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		EntityFakeArrow entityarrow = new EntityFakeArrow(this.world, this);
		double x = target.posX - this.posX;
		double y = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
		double z = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt(x * x + z * z);
		entityarrow.setThrowableHeading(x, y + d3 * 0.2D, z, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));

		DifficultyInstance difficultyinstance = this.world.getDifficultyForLocation(new BlockPos(this));
		entityarrow.setDamage((double)(distanceFactor * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().getDifficultyId() * 0.11F));

		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}

}
