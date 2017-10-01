package net.crazysnailboy.mods.halloween.common.config;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.common.network.ConfigSyncMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;


public class ModConfiguration
{

	private static class DefaultValues
	{
		// general
		private static final boolean legacyMode = false;
		// halloween
		private static final boolean alwaysHalloween = false;
		private static final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 10, 1);
		private static final LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 11, 1);
		private static final boolean isHalloween = false;
		private static final int halloweenCheckInterval = 60;
		// sounds
		private static final boolean ambientSoundsEnabled = true;
		private static final int ambientSoundInterval = 900;
		// entities
		private static final boolean enableCreeperweens = true;
		private static final boolean invulnerableCreeperweens = true;
		private static final boolean enableHallowitches = true;
		private static final boolean enableHaunters = true;
		private static final boolean enableJumpkins = true;
		private static final boolean jumpkinsDespawnDuringDaytime = true;
		private static final boolean jumpkinsTurnToPumpkins = true;
		private static final boolean enableZombieHands = true;
		private static final boolean enableTreaters = true;
		private static final boolean treatersDespawnDuringDaytime = true;
		private static final boolean enableCurses = true;
	}

	private static Configuration config = null;

	// categories
	public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
	public static final String CATEGORY_HALLOWEEN = "halloween";
	public static final String CATEGORY_SOUNDS = "sounds";
	public static final String CATEGORY_ENTITIES = "entities";

	// property values
	// general
	public static boolean legacyMode = DefaultValues.legacyMode;
	// halloween
	public static boolean alwaysHalloween = DefaultValues.alwaysHalloween;
	public static LocalDate startDate = DefaultValues.startDate;
	public static LocalDate endDate = DefaultValues.endDate;
	public static boolean isHalloween = DefaultValues.isHalloween;
	public static int halloweenCheckInterval = DefaultValues.halloweenCheckInterval;
	// sounds
	public static boolean ambientSoundsEnabled = DefaultValues.ambientSoundsEnabled;
	public static int ambientSoundInterval = DefaultValues.ambientSoundInterval;
	// entities
	// creeperweens
	public static boolean enableCreeperweens = DefaultValues.enableCreeperweens;
	public static boolean invulnerableCreeperweens = DefaultValues.invulnerableCreeperweens;
	// hallowitches
	public static boolean enableHallowitches = DefaultValues.enableHallowitches;
	// haunters
	public static boolean enableHaunters = DefaultValues.enableHaunters;
	// jumpkins
	public static boolean enableJumpkins = DefaultValues.enableJumpkins;
	public static boolean jumpkinsDespawnDuringDaytime = DefaultValues.jumpkinsDespawnDuringDaytime;
	public static boolean jumpkinsTurnToPumpkins = DefaultValues.jumpkinsTurnToPumpkins;
	// zombie hands
	public static boolean enableZombieHands = DefaultValues.enableZombieHands;
	// treaters
	public static boolean enableTreaters = DefaultValues.enableTreaters;
	public static boolean treatersDespawnDuringDaytime = DefaultValues.treatersDespawnDuringDaytime;
	// curses
	public static boolean enableCurses = DefaultValues.enableCurses;


	public static void initializeConfiguration()
	{
		// load the configuration from the file
		config = new Configuration(new File(Loader.instance().getConfigDir(), HalloweenMod.MODID + ".cfg"));
		config.load();


		// get the configuration properties
		// general
		config.addCustomCategoryComment(CATEGORY_GENERAL, "~");
		Property propLegacyMode = config.get(CATEGORY_GENERAL, "legacyMode", DefaultValues.legacyMode, "Use behaviour from the original mod where the revived mod is different. Default is " + DefaultValues.legacyMode + ".");
		// halloween
		config.addCustomCategoryComment(CATEGORY_HALLOWEEN, "~");
		Property propAlwaysHalloween = config.get(CATEGORY_HALLOWEEN, "alwaysHalloween", DefaultValues.alwaysHalloween, "Is it Halloween all year round?. Default is " + DefaultValues.alwaysHalloween + ".");
		Property propStartDate = config.get(CATEGORY_HALLOWEEN, "startDate", LocalDateToString(DefaultValues.startDate), "The date on which Halloween starts each year (MM-dd). Default is \"" + LocalDateToString(DefaultValues.startDate) + "\".");
		Property propEndDate = config.get(CATEGORY_HALLOWEEN, "endDate", LocalDateToString(DefaultValues.endDate), "The date on which Halloween ends each year (MM-dd). Default is \"" + LocalDateToString(DefaultValues.endDate) + "\".");
		Property propHalloweenCheckInterval = config.get(CATEGORY_HALLOWEEN, "halloweenCheckInterval", DefaultValues.halloweenCheckInterval, "The frequency with which to check if it's currently Halloween, in minutes.\nDefault is " + DefaultValues.halloweenCheckInterval + ", use 0 to disable.");
		// sounds
		config.addCustomCategoryComment(CATEGORY_SOUNDS, "~");
		Property propAmbientSoundsEnabled = config.get(CATEGORY_SOUNDS, "ambientSoundsEnabled", DefaultValues.ambientSoundsEnabled, "Play ambient sound effects. Default is " + DefaultValues.ambientSoundsEnabled + ".");
		Property propAmbientSoundInterval = config.get(CATEGORY_SOUNDS, "ambientSoundInterval", DefaultValues.ambientSoundInterval, "Frequency with which ambient sound effects are played. Default is " + DefaultValues.ambientSoundInterval + " ticks.");
		// entities
		config.addCustomCategoryComment(CATEGORY_ENTITIES, "~");
		// creeperweens
		Property propEnableCreeperweens = config.get(CATEGORY_ENTITIES, "enableCreeperweens", DefaultValues.enableCreeperweens, "Should Halloween Creepers naturally spawn? Default is " + DefaultValues.enableCreeperweens + ".");
		Property propInvulnerableCreeperweens = config.get(CATEGORY_ENTITIES, "invulnerableCreeperweens", DefaultValues.invulnerableCreeperweens, "Are Halloween Creepers invulnerable? Default is " + DefaultValues.invulnerableCreeperweens + ".");
		// hallowitches
		Property propEnableHallowitches = config.get(CATEGORY_ENTITIES, "enableHallowitches", DefaultValues.enableHallowitches, "Should Halloween Witches naturally spawn? Default is " + DefaultValues.enableHallowitches + ".");
		// haunters
		Property propEnableHaunters = config.get(CATEGORY_ENTITIES, "enableHaunters", DefaultValues.enableHaunters, "Should Haunters (Ghouls) naturally spawn?. Default is " + DefaultValues.enableHaunters + ".");
		// jumpkins
		Property propEnableJumpkins = config.get(CATEGORY_ENTITIES, "enableJumpkins", DefaultValues.enableJumpkins, "Should Jumpkins naturally spawn?. Default is " + DefaultValues.enableJumpkins + ".");
		Property propJumpkinsDespawnDuringDaytime = config.get(CATEGORY_ENTITIES, "jumpkinsDespawnDuringDaytime", DefaultValues.jumpkinsDespawnDuringDaytime, "Should Jumpkins despawn when it becomes daytime?. Default is " + DefaultValues.jumpkinsDespawnDuringDaytime + ".");
		Property propJumpkinsTurnToPumpkins = config.get(CATEGORY_ENTITIES, "jumpkinsTurnToPumpkins", DefaultValues.jumpkinsTurnToPumpkins, "Should Jumpkins turn to Pumpkins when they despawn? (Requires mobGriefing=true). Default is " + DefaultValues.jumpkinsTurnToPumpkins + ".");
		// zombie hands
		Property propEnableZombieHands = config.get(CATEGORY_ENTITIES, "enableZombieHands", DefaultValues.enableZombieHands, "Should Zombie Hands naturally spawn?. Default is " + DefaultValues.enableZombieHands + ".");
		// treaters
		Property propEnableTreaters = config.get(CATEGORY_ENTITIES, "enableTreaters", DefaultValues.enableTreaters, "Should Trick or Treaters naturally spawn?. Default is " + DefaultValues.enableTreaters + ".");
		Property propTreatersDespawnDuringDaytime = config.get(CATEGORY_ENTITIES, "treatersDespawnDuringDaytime", DefaultValues.treatersDespawnDuringDaytime, "Should Trick or Treaters despawn when it becomes daytime?. Default is " + DefaultValues.treatersDespawnDuringDaytime + ".");
		// curses
		Property propEnableCurses = config.get(CATEGORY_ENTITIES, "enableCurses", DefaultValues.enableCurses, "Should being struck by a Curse Orb curse the target?. Default is " + DefaultValues.enableCurses + ".");


		// set the category property order
		// general
		List<String> propOrderGeneral = new ArrayList<String>();
		propOrderGeneral.add(propLegacyMode.getName());
		config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrderGeneral);
		// halloween
		List<String> propOrderHalloween = new ArrayList<String>();
		propOrderHalloween.add(propAlwaysHalloween.getName());
		propOrderHalloween.add(propStartDate.getName());
		propOrderHalloween.add(propEndDate.getName());
		propOrderHalloween.add(propHalloweenCheckInterval.getName());
		config.setCategoryPropertyOrder(CATEGORY_HALLOWEEN, propOrderHalloween);
		// sounds
		List<String> propOrderSounds = new ArrayList<String>();
		propOrderSounds.add(propAmbientSoundsEnabled.getName());
		propOrderSounds.add(propAmbientSoundInterval.getName());
		config.setCategoryPropertyOrder(CATEGORY_SOUNDS, propOrderSounds);
		// entities
		List<String> propOrderEntities = new ArrayList<String>();
		propOrderEntities.add(propEnableCreeperweens.getName());
		propOrderEntities.add(propInvulnerableCreeperweens.getName());
		propOrderEntities.add(propEnableHallowitches.getName());
		propOrderEntities.add(propEnableHaunters.getName());
		propOrderEntities.add(propEnableJumpkins.getName());
		propOrderEntities.add(propJumpkinsDespawnDuringDaytime.getName());
		propOrderEntities.add(propJumpkinsTurnToPumpkins.getName());
		propOrderEntities.add(propEnableZombieHands.getName());
		propOrderEntities.add(propEnableTreaters.getName());
		propOrderEntities.add(propTreatersDespawnDuringDaytime.getName());
		propOrderEntities.add(propEnableCurses.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENTITIES, propOrderSounds);


		// read the property values
		// general
		legacyMode = propLegacyMode.getBoolean();
		// halloween
		alwaysHalloween = propAlwaysHalloween.getBoolean();
		startDate = StringToLocalDate(propStartDate.getString());
		endDate = StringToLocalDate(propEndDate.getString());
		isHalloween = isHalloween();
		halloweenCheckInterval = propHalloweenCheckInterval.getInt();
		// sounds
		ambientSoundsEnabled = propAmbientSoundsEnabled.getBoolean();
		ambientSoundInterval = propAmbientSoundInterval.getInt();
		// entities
		// creeperweens
		enableCreeperweens = propEnableCreeperweens.getBoolean();
		invulnerableCreeperweens = propInvulnerableCreeperweens.getBoolean();
		// hallowitches
		enableHallowitches = propEnableHallowitches.getBoolean();
		// haunters
		enableHaunters = propEnableHaunters.getBoolean();
		// jumpkins
		enableJumpkins = propEnableJumpkins.getBoolean();
		jumpkinsDespawnDuringDaytime = propJumpkinsDespawnDuringDaytime.getBoolean();
		jumpkinsTurnToPumpkins = propJumpkinsTurnToPumpkins.getBoolean();
		// zombie hands
		enableZombieHands = propEnableZombieHands.getBoolean();
		// treaters
		enableTreaters = propEnableTreaters.getBoolean();
		treatersDespawnDuringDaytime = propTreatersDespawnDuringDaytime.getBoolean();
		// curses
		enableCurses = propEnableCurses.getBoolean();


		// save the configuration to the file
		config.save();
	}

	public static boolean isHalloween()
	{
		return (alwaysHalloween ? true : (!LocalDate.now().isBefore(ModConfiguration.startDate) && !LocalDate.now().isAfter(ModConfiguration.endDate)));
	}

	public static void readFromNBT(NBTTagCompound compound)
	{
		// general
		legacyMode = (compound.hasKey("legacyMode") ? compound.getBoolean("legacyMode") : DefaultValues.legacyMode);
		// halloween
		alwaysHalloween = (compound.hasKey("alwaysHalloween") ? compound.getBoolean("alwaysHalloween") : DefaultValues.alwaysHalloween);
		startDate = (compound.hasKey("startDate") ? StringToLocalDate(compound.getString("startDate")) : DefaultValues.startDate);
		endDate = (compound.hasKey("endDate") ? StringToLocalDate(compound.getString("endDate")) : DefaultValues.endDate);
		isHalloween = (compound.hasKey("isHalloween") ? compound.getBoolean("isHalloween") : isHalloween());
		halloweenCheckInterval = (compound.hasKey("halloweenCheckInterval") ? compound.getInteger("halloweenCheckInterval") : DefaultValues.halloweenCheckInterval);
		// sounds
		ambientSoundsEnabled = (compound.hasKey("ambientSoundsEnabled") ? compound.getBoolean("ambientSoundsEnabled") : DefaultValues.ambientSoundsEnabled);
		ambientSoundInterval = (compound.hasKey("ambientSoundInterval") ? compound.getInteger("ambientSoundInterval") : DefaultValues.ambientSoundInterval);
		// entities
		enableCreeperweens = (compound.hasKey("enableCreeperweens") ? compound.getBoolean("enableCreeperweens") : DefaultValues.enableCreeperweens);
		invulnerableCreeperweens = (compound.hasKey("invulnerableCreeperweens") ? compound.getBoolean("invulnerableCreeperweens") : DefaultValues.invulnerableCreeperweens);
		enableHallowitches = (compound.hasKey("enableHallowitches") ? compound.getBoolean("enableHallowitches") : DefaultValues.enableHallowitches);
		enableHaunters = (compound.hasKey("enableHaunters") ? compound.getBoolean("enableHaunters") : DefaultValues.enableHaunters);
		enableJumpkins = (compound.hasKey("enableJumpkins") ? compound.getBoolean("enableJumpkins") : DefaultValues.enableJumpkins);
		jumpkinsDespawnDuringDaytime = (compound.hasKey("jumpkinsDespawnDuringDaytime") ? compound.getBoolean("jumpkinsDespawnDuringDaytime") : DefaultValues.jumpkinsDespawnDuringDaytime);
		jumpkinsTurnToPumpkins = (compound.hasKey("jumpkinsTurnToPumpkins") ? compound.getBoolean("jumpkinsTurnToPumpkins") : DefaultValues.jumpkinsTurnToPumpkins);
		enableZombieHands = (compound.hasKey("enableZombieHands") ? compound.getBoolean("enableZombieHands") : DefaultValues.enableZombieHands);
		enableTreaters = (compound.hasKey("enableTreaters") ? compound.getBoolean("enableTreaters") : DefaultValues.enableTreaters);
		treatersDespawnDuringDaytime = (compound.hasKey("treatersDespawnDuringDaytime") ? compound.getBoolean("treatersDespawnDuringDaytime") : DefaultValues.treatersDespawnDuringDaytime);
		enableCurses = (compound.hasKey("enableCurses") ? compound.getBoolean("enableCurses") : DefaultValues.enableCurses);
	}

	public static NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		// general
		compound.setBoolean("legacyMode", legacyMode);
		// halloween
		compound.setBoolean("alwaysHalloween", alwaysHalloween);
		compound.setString("startDate", LocalDateToString(startDate));
		compound.setString("endDate", LocalDateToString(endDate));
		compound.setBoolean("isHalloween", isHalloween);
		compound.setInteger("halloweenCheckInterval", halloweenCheckInterval);
		// sounds
		compound.setBoolean("ambientSoundsEnabled", ambientSoundsEnabled);
		compound.setInteger("ambientSoundInterval", ambientSoundInterval);
		// entities
		compound.setBoolean("enableCreeperweens", enableCreeperweens);
		compound.setBoolean("invulnerableCreeperweens", invulnerableCreeperweens);
		compound.setBoolean("enableHallowitches", enableHallowitches);
		compound.setBoolean("enableHaunters", enableHaunters);
		compound.setBoolean("enableJumpkins", enableJumpkins);
		compound.setBoolean("jumpkinsDespawnDuringDaytime", jumpkinsDespawnDuringDaytime);
		compound.setBoolean("jumpkinsTurnToPumpkins", jumpkinsTurnToPumpkins);
		compound.setBoolean("enableZombieHands", enableZombieHands);
		compound.setBoolean("enableTreaters", enableTreaters);
		compound.setBoolean("treatersDespawnDuringDaytime", treatersDespawnDuringDaytime);
		compound.setBoolean("enableCurses", enableCurses);

		return compound;
	}


	@EventBusSubscriber
	public static class ConfigEventHandler
	{
		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerLoggedInEvent event)
		{
			if (!event.player.world.isRemote)
			{
				NBTTagCompound compound = ModConfiguration.writeToNBT(new NBTTagCompound());
				HalloweenMod.NETWORK.sendTo(new ConfigSyncMessage(compound), (EntityPlayerMP)event.player);
			}
		}
	}

	private static LocalDate StringToLocalDate(String value)
	{
		return LocalDate.parse(String.valueOf(LocalDate.now().getYear()) + "-" + value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	private static String LocalDateToString(LocalDate value)
	{
		return value.format(DateTimeFormatter.ofPattern("MM-dd"));
	}

}