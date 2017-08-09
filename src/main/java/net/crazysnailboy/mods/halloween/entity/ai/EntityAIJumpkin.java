package net.crazysnailboy.mods.halloween.entity.ai;

import java.lang.reflect.Method;
import net.crazysnailboy.mods.halloween.entity.monster.EntityJumpkin;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;


/**
 * AI tasks for {@link EntityJumpkin}
 * Adapted from {@link net.minecraft.entity.monster.EntitySlime$AISlimeFaceRandom} and {@link net.minecraft.entity.monster.EntitySlime$AISlimeHop},
 * with some hacky reflection :)
 *
 */
public class EntityAIJumpkin
{

	private static final Class SlimeMoveHelper = ReflectionUtils.getClass("net.minecraft.entity.monster.EntitySlime$SlimeMoveHelper");


	/**
	 * Modified version of {@link net.minecraft.entity.monster.EntitySlime$AISlimeFaceRandom}
	 */
	public static class FaceRandom extends EntityAIBase
	{

		private static final Method setDirection = ReflectionUtils.getDeclaredMethod(SlimeMoveHelper, new String[] { "setDirection", "func_179920_a" }, float.class, boolean.class);

		private final EntityJumpkin entity;
		private float chosenDegrees;
		private int nextRandomizeTime;


		public FaceRandom(EntityJumpkin entity)
		{
			this.entity = entity;
			this.setMutexBits(2);
		}


		@Override
		public boolean shouldExecute()
		{
			double followRange = this.entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			EntityPlayer player = this.entity.world.getClosestPlayerToEntity(this.entity, followRange);

			boolean shouldExecute = (player != null && this.entity.getAttackTarget() == null && (this.entity.onGround || this.entity.isInWater() || this.entity.isInLava() || this.entity.isPotionActive(MobEffects.LEVITATION)));
			if (!shouldExecute) this.entity.setLit(false);
			return shouldExecute;
		}

		@Override
		public void startExecuting()
		{
			if (!this.entity.getAwakened()) this.entity.setAwakened(true);
			this.entity.setLit(true);
		}

		@Override
		public void resetTask()
		{
			this.chosenDegrees = (float)(this.entity.getRNG().nextInt(4) * 90.0F);
			ReflectionUtils.invokeMethod(setDirection, this.entity.getMoveHelper(), this.chosenDegrees, false);
		}

		@Override
		public void updateTask()
		{
			if (--this.nextRandomizeTime <= 0)
			{
				this.nextRandomizeTime = 40 + this.entity.getRNG().nextInt(60);
				this.chosenDegrees = (float)this.entity.getRNG().nextInt(360);
			}
			ReflectionUtils.invokeMethod(setDirection, this.entity.getMoveHelper(), this.chosenDegrees, false);
		}

	}


	/**
	 * Modified version of {@link net.minecraft.entity.monster.EntitySlime$AISlimeHop}
	 */
	public static class Hop extends EntityAIBase
	{

		private static final Method setSpeed = ReflectionUtils.getDeclaredMethod(SlimeMoveHelper, new String[] { "setSpeed", "func_179921_a" }, double.class);

		private final EntityJumpkin entity;


		public Hop(EntityJumpkin entity)
		{
			this.entity = entity;
			this.setMutexBits(5);
		}

		@Override
		public boolean shouldExecute()
		{
			double followRange = this.entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			EntityPlayer player = this.entity.world.getClosestPlayerToEntity(this.entity, followRange);

			boolean shouldExecute = (player != null);
			if (!shouldExecute) this.entity.setLit(false);
			return shouldExecute;
		}

		@Override
		public void startExecuting()
		{
			if (!this.entity.getAwakened()) this.entity.setAwakened(true);
			this.entity.setLit(true);
		}

		@Override
		public void resetTask()
		{
//			int posX = this.entity.getPosition().getX();
//			int posZ = this.entity.getPosition().getZ();
//			this.entity.setPositionAndUpdate(posX, this.entity.posY, posZ);
//			this.entity.setPositionAndUpdate(((long)this.entity.posX) + 0.5D, this.entity.posY, ((long)this.entity.posZ) + 0.5D);
		}

		@Override
		public void updateTask()
		{
			ReflectionUtils.invokeMethod(setSpeed, this.entity.getMoveHelper(), 1.0D);
		}
	}

}