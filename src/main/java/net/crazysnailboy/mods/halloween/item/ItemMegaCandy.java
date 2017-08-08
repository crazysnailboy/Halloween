package net.crazysnailboy.mods.halloween.item;

import java.util.List;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemMegaCandy extends ItemFood
{

	public ItemMegaCandy()
	{
		super(10, 0.3F, false);
		this.setAlwaysEdible();
	}


	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		player.heal(5.0F);

		List<EntityCurse> entitiesCurse = world.getEntitiesWithinAABB(EntityCurse.class, player.getEntityBoundingBox().expand(4D, 4D, 4D));
		for (EntityCurse entityCurse : entitiesCurse)
		{
			if (entityCurse.victim == player)
			{
				entityCurse.victim = null;
				entityCurse.setDead();
			}
		}
	}

}
