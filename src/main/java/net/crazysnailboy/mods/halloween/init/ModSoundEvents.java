package net.crazysnailboy.mods.halloween.init;

import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.util.ReflectionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class ModSoundEvents
{

	public static final SoundEvent AMBIENT_HALLOWEEN = createSoundEvent("ambient.halloween");
	public static final SoundEvent AMBIENT_OOO = createSoundEvent("ambient.ooo");
	public static final SoundEvent AMBIENT_SPOOKY = createSoundEvent("ambient.spooky");

	public static final SoundEvent ENTITY_FAKE_FADE = createSoundEvent("entity.fake.fade");

	public static final SoundEvent ENTITY_HALLOWITCH_AMBIENT = createSoundEvent("entity.hallowitch.ambient");
	public static final SoundEvent ENTITY_HALLOWITCH_DEATH = createSoundEvent("entity.hallowitch.death");
	public static final SoundEvent ENTITY_HALLOWITCH_HURT = createSoundEvent("entity.hallowitch.hurt");

	public static final SoundEvent ENTITY_HAUNTER_APPEAR = createSoundEvent("entity.haunter.appear");
	public static final SoundEvent ENTITY_HAUNTER_DEATH = createSoundEvent("entity.haunter.death");
	public static final SoundEvent ENTITY_HAUNTER_HURT = createSoundEvent("entity.haunter.hurt");

	public static final SoundEvent ITEM_MONSTER_DETECTOR_HIGH = createSoundEvent("item.monster_detector.detecthi");
	public static final SoundEvent ITEM_MONSTER_DETECTOR_LOW = createSoundEvent("item.monster_detector.detectlo");


	@SubscribeEvent
	public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event)
	{
		SoundEvent[] soundEvents = ReflectionUtils.getDeclaredFields(SoundEvent.class, ModSoundEvents.class);
		event.getRegistry().registerAll(soundEvents);
	}


	private static SoundEvent createSoundEvent(String name)
	{
		final ResourceLocation registryName = new ResourceLocation(HalloweenMod.MODID, name);
		return new SoundEvent(registryName).setRegistryName(registryName);
	}



//	public static class ModSoundEvent extends SoundEvent
//	{
//
//		public ModSoundEvent(final String soundName)
//		{
//			this(new ResourceLocation(HalloweenMod.MODID, soundName));
//		}
//
//		public ModSoundEvent(final ResourceLocation soundName)
//		{
//			super(soundName);
//			this.setRegistryName(soundName);
//		}
//
//	}

}