package net.crazysnailboy.mods.halloween.entity.projectile.fake;

import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSkeleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


/**
 * A "fake" arrow. Used by {@link EntityFakeSkeleton} during it's attack.
 *
 */
public class EntityFakeArrow extends EntityArrow
{

	public EntityFakeArrow(World world)
	{
		super(world);
		this.setDamage(0.0D);
	}

	public EntityFakeArrow(World world, double x, double y, double z)
	{
		super(world, x, y, z);
		this.setDamage(0.0D);
	}

	public EntityFakeArrow(World world, EntityLivingBase shooter)
	{
		super(world, shooter);
		this.setDamage(0.0D);
	}

	@Override
	protected ItemStack getArrowStack()
	{
		return null;
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
	{
		this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		this.setDead();
	}

}
