package net.crazysnailboy.mods.halloween.entity.effect;

import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;


public class EntityZombieCurse extends EntityCurse
{

	public EntityZombieCurse(World world)
	{
		this(world, null);
	}

	public EntityZombieCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.lifetime > 0 && this.victim != null && legacyMode())
		{
			this.performLegacyCurse();
		}
	}

	@Override
	public void performCurse()
	{
		super.performCurse();
		if (this.lifetime > 0 && this.victim != null && !legacyMode())
		{
			this.performRevivalCurse();
		}
	}

	@Override
	public void doTickSound()
	{
		this.playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 0.75F, (this.rand.nextFloat() * 0.4F) + 0.8F);
	}

	@Override
	public EnumCurseType getCurseType()
	{
		return EnumCurseType.ZOMBIE;
	}

	private boolean legacyMode()
	{
		return (ModConfiguration.legacyMode && this.victim instanceof EntityPlayer);
	}

	/**
	 * Original KodaichiZero zombie curse - slow the player down and reduce their jump height
	 */
	private void performLegacyCurse()
	{
		this.victim.motionX *= 0.625D;
		this.victim.motionZ *= 0.625D;

		if (!this.victim.onGround && this.victim.motionY > 0D)
		{
			this.victim.motionY -= 0.06D;
		}
	}

	/**
	 * Revival zombie curse - fill the player's inventory with poisionous potatoes
	 */
	private void performRevivalCurse()
	{
		EntityPlayer player = (EntityPlayer)this.victim;
		ItemStack stack = new ItemStack(Items.POISONOUS_POTATO, 16 + this.rand.nextInt(17), 0);
		EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);

		if (player.inventory.addItemStackToInventory(stack))
		{
			ForgeEventFactory.onItemPickup(entityItem, player);
			player.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 0.75F, (this.rand.nextFloat() * 0.2F) + 0.9F);
		}
	}

}