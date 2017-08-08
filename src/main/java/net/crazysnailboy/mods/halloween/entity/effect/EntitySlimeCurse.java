package net.crazysnailboy.mods.halloween.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;


public class EntitySlimeCurse extends EntityCurse
{

	public EntitySlimeCurse(World world)
	{
		this(world, null);
	}

	public EntitySlimeCurse(World world, EntityLivingBase victim)
	{
		super(world, victim);
	}


	@Override
	public void performCurse()
	{
		super.performCurse();
		this.lift = 8;
//		if (this.lifetime > 0 && this.victim != null && this.victim instanceof EntityPlayer)
//		{
//			EntityPlayer player = (EntityPlayer)this.victim;
//
//			ItemStack stack = new ItemStack(Items.SLIME_BALL, 16 + this.rand.nextInt(17), 0);
//			EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
//
//			if (player.inventory.addItemStackToInventory(stack))
//			{
//				ForgeEventFactory.onItemPickup(entityItem, player, stack);
//				player.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 0.75F, (this.rand.nextFloat() * 0.2F) + 0.9F); // "mob.slimeattack"
//			}
//		}
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

}