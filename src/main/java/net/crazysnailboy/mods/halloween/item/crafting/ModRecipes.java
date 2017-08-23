package net.crazysnailboy.mods.halloween.item.crafting;

import net.crazysnailboy.mods.halloween.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes
{

	public static void registerCraftingRecipes()
	{
		// mega candy
		GameRegistry.addShapelessRecipe(
			new ItemStack(ModItems.MEGA_CANDY),
			new Object[] { new ItemStack(ModItems.CANDY, 1, 0), new ItemStack(ModItems.CANDY, 1, 1), new ItemStack(ModItems.CANDY, 1, 2), new ItemStack(ModItems.CANDY, 1, 3), new ItemStack(ModItems.CANDY, 1, 4) }
		);

		// monster detector
		GameRegistry.addRecipe(
			new ItemStack(ModItems.MONSTER_DETECTOR),
			new Object[] { "MD", "DM", 'M', ModItems.HAUNT_MALEV, 'D', ModItems.HAUNT_DIABOL }
		);
		GameRegistry.addRecipe(
			new ItemStack(ModItems.MONSTER_DETECTOR),
			new Object[] { "DM", "MD", 'M', ModItems.HAUNT_MALEV, 'D', ModItems.HAUNT_DIABOL }
		);

	}

}