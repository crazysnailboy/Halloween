package net.crazysnailboy.mods.halloween.init;

import net.crazysnailboy.mods.halloween.creativetab.ModCreativeTabs;
import net.crazysnailboy.mods.halloween.item.IMultiItem;
import net.crazysnailboy.mods.halloween.item.ISubItem;
import net.crazysnailboy.mods.halloween.item.ItemCandy;
import net.crazysnailboy.mods.halloween.item.ItemCurseOrb;
import net.crazysnailboy.mods.halloween.item.ItemMegaCandy;
import net.crazysnailboy.mods.halloween.item.ItemMonsterDetector;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@EventBusSubscriber
public class ModItems
{

	public static final Item CANDY = new ItemCandy().setRegistryName("candy").setUnlocalizedName("halloweenCandy").setCreativeTab(ModCreativeTabs.HALLOWEEN);
	public static final Item MEGA_CANDY = new ItemMegaCandy().setRegistryName("mega_candy").setUnlocalizedName("megaCandy").setCreativeTab(ModCreativeTabs.HALLOWEEN);
	public static final Item HAUNT_MALEV = new Item().setRegistryName("haunt_malev").setUnlocalizedName("hauntMalev").setCreativeTab(ModCreativeTabs.HALLOWEEN);
	public static final Item HAUNT_DIABOL = new Item().setRegistryName("haunt_diabol").setUnlocalizedName("hauntDiabol").setCreativeTab(ModCreativeTabs.HALLOWEEN);
	public static final Item MONSTER_DETECTOR = new ItemMonsterDetector().setRegistryName("monster_detector").setUnlocalizedName("monsterDetector").setCreativeTab(ModCreativeTabs.HALLOWEEN);
	public static final Item CURSE_ORB = new ItemCurseOrb().setRegistryName("curse_orb").setUnlocalizedName("curseOrb").setCreativeTab(ModCreativeTabs.HALLOWEEN);

	private static final Item[] items = ReflectionUtils.getDeclaredFields(Item.class, ModItems.class);


	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerInventoryModels(final ModelRegistryEvent event)
	{
		for (Item item : items)
		{
			if (item instanceof IMultiItem)
			{
				final Class enumClass = (Class<?>)ReflectionUtils.getGenericInterfaceType(item.getClass(), IMultiItem.class);
				for (ISubItem value : (ISubItem[])enumClass.getEnumConstants())
				{
					ModelLoader.setCustomModelResourceLocation(item, value.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory"));
				}
			}
			else
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}

	}


	@SideOnly(Side.CLIENT)
	public static void registerColorHandlers()
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCandy.ColorHandler(), CANDY);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCurseOrb.ColorHandler(), CURSE_ORB);
	}

}
