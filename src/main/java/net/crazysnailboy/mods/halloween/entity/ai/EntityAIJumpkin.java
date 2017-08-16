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

		private final EntityJumpkin taskOwner;
		private float chosenDegrees;
		private int nextRandomizeTime;


		public FaceRandom(EntityJumpkin taskOwner)
		{
			this.taskOwner = taskOwner;
			this.setMutexBits(2);
		}


		@Override
		public boolean shouldExecute()
		{
			double followRange = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			EntityPlayer player = this.taskOwner.world.getClosestPlayerToEntity(this.taskOwner, followRange);
			return (player != null && this.taskOwner.getAttackTarget() == null && (this.taskOwner.onGround || this.taskOwner.isInWater() || this.taskOwner.isInLava() || this.taskOwner.isPotionActive(MobEffects.LEVITATION)));
		}

		@Override
		public void updateTask()
		{
			if (--this.nextRandomizeTime <= 0)
			{
				this.nextRandomizeTime = 40 + this.taskOwner.getRNG().nextInt(60);
				this.chosenDegrees = (float)this.taskOwner.getRNG().nextInt(360);
			}
			ReflectionUtils.invokeMethod(setDirection, this.taskOwner.getMoveHelper(), this.chosenDegrees, false);
		}

	}


	/**
	 * Modified version of {@link net.minecraft.entity.monster.EntitySlime$AISlimeHop}
	 */
	public static class Hop extends EntityAIBase
	{

		private static final Method setSpeed = ReflectionUtils.getDeclaredMethod(SlimeMoveHelper, new String[] { "setSpeed", "func_179921_a" }, double.class);

		private final EntityJumpkin taskOwner;


		public Hop(EntityJumpkin taskOwner)
		{
			this.taskOwner = taskOwner;
			this.setMutexBits(5);
		}


		@Override
		public boolean shouldExecute()
		{
			double followRange = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
			EntityPlayer player = this.taskOwner.world.getClosestPlayerToEntity(this.taskOwner, followRange);
			return (player != null);
		}

		@Override
		public void startExecuting()
		{
			if (!this.taskOwner.getAwakened()) this.taskOwner.setAwakened(true);
			this.taskOwner.setLit(true);
		}

		@Override
		public void resetTask()
		{
			this.taskOwner.setLit(false);
//			this.taskOwner.rotationPitch = 0.0F;
//			this.taskOwner.rotationYaw = this.taskOwner.prevRotationYaw = this.taskOwner.rotationYawHead = this.taskOwner.renderYawOffset = (Math.round(this.taskOwner.rotationYaw / 90.0F) * 90.0F);
//			this.taskOwner.moveToBlockPosAndAngles(this.taskOwner.getPosition(), this.taskOwner.rotationYaw, this.taskOwner.rotationPitch);
//			HalloweenMod.NETWORK.sendToDimension(new JumpkinRotationMessage(this.taskOwner), this.taskOwner.dimension);
		}

		@Override
		public void updateTask()
		{
			ReflectionUtils.invokeMethod(setSpeed, this.taskOwner.getMoveHelper(), 1.0D);
		}
	}

}