package net.crazysnailboy.mods.halloween.entity.effect;

import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class EntitySlimeCurse extends EntityCurse
{

	private EventHandlers eventHandlers = null;

	public EntitySlimeCurse(World world)
	{
		this(world, null);
	}

	public EntitySlimeCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.lifetime > 0 && this.victim != null && !legacyMode())
		{
			this.performRevivalCurse();
		}
	}

	@Override
	public void performCurse()
	{
		super.performCurse();
		if (this.lifetime > 0 && this.victim != null && legacyMode())
		{
			this.performLegacyCurse();
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_SLIME_HURT, 0.5F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.SLIME;
	}

	@Override
	public void setDead()
	{
		if (eventHandlers != null)
		{
			MinecraftForge.EVENT_BUS.unregister(eventHandlers);
		}
		super.setDead();
	}

	private boolean legacyMode()
	{
		return (ModConfiguration.legacyMode && this.victim instanceof EntityPlayer);
	}

	/**
	 * Original KodaichiZero slime curse - fill the player's inventory with slime balls
	 */
	private void performLegacyCurse()
	{
		EntityPlayer player = (EntityPlayer)this.victim;
		ItemStack stack = new ItemStack(Items.SLIME_BALL, 16 + this.rand.nextInt(17), 0);
		EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);

		if (player.inventory.addItemStackToInventory(stack))
		{
			ForgeEventFactory.onItemPickup(entityItem, player, stack);
			player.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 0.75F, (this.rand.nextFloat() * 0.2F) + 0.9F);
		}
	}

	/**
	 * Revival slime curse - slow the player down and increase their jump height
	 */
	private void performRevivalCurse()
	{
		if (eventHandlers == null)
		{
			eventHandlers = new EventHandlers(this.victim);
			MinecraftForge.EVENT_BUS.register(eventHandlers);
		}
		this.victim.motionX *= 0.625D;
		this.victim.motionZ *= 0.625D;
	}


	public static class EventHandlers
	{
		private EntityLivingBase victim;

		public EventHandlers(EntityLivingBase victim)
		{
			this.victim = victim;
		}

		@SubscribeEvent
		public void onLivingJump(LivingJumpEvent event)
		{
			if (event.getEntity() == this.victim)
			{
				event.getEntity().motionY += 0.2D;
			}
		}
	}

}