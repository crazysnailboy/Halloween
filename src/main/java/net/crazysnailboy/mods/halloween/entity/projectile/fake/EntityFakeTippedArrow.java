package net.crazysnailboy.mods.halloween.entity.projectile.fake;

import java.util.Collections;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityFakeTippedArrow extends EntityTippedArrow
{

    private static final DataParameter<Integer> COLOR = ReflectionUtils.getFieldValue(ReflectionUtils.getDeclaredField(EntityTippedArrow.class, "COLOR", "field_184559_f"), null);


    public EntityFakeTippedArrow(World world)
	{
		super(world);
		this.setDamage(0.0D);
	}

	public EntityFakeTippedArrow(World world, double x, double y, double z)
	{
		super(world, x, y, z);
		this.setDamage(0.0D);
	}

	public EntityFakeTippedArrow(World world, EntityLivingBase shooter)
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
	protected void onHit(RayTraceResult result)
	{
		this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		this.setDead();
	}

    @Override
	public void addEffect(PotionEffect effect)
    {
    	int color = PotionUtils.getPotionColorFromEffectList(Collections.singletonList(effect));
    	this.getDataManager().set(COLOR, color);
    }

}