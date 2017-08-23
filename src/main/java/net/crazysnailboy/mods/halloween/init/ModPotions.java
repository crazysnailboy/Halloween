package net.crazysnailboy.mods.halloween.init;

import net.crazysnailboy.mods.halloween.potion.PotionCurse;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;


//@EventBusSubscriber
public class ModPotions
{

	public static final Potion CREEPER_CURSE = new PotionCurse().setRegistryName("creeper_curse").setPotionName("effect.creeperCurse");
	public static final Potion GHAST_CURSE = new PotionCurse().setRegistryName("ghast_curse").setPotionName("effect.ghastCurse");
	public static final Potion SKELETON_CURSE = new PotionCurse().setRegistryName("skeleton_curse").setPotionName("effect.skeletonCurse");
	public static final Potion SPIDER_CURSE = new PotionCurse().setRegistryName("spider_curse").setPotionName("effect.spiderCurse");
	public static final Potion ZOMBIE_CURSE = new PotionCurse().setRegistryName("zombie_curse").setPotionName("effect.zombieCurse");


//	@SubscribeEvent
	public static void registerPotions(final RegistryEvent.Register<Potion> event)
	{
		Potion[] potions = ReflectionUtils.getDeclaredFields(Potion.class, ModPotions.class);
		event.getRegistry().registerAll(potions);
	}

}