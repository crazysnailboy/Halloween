package net.crazysnailboy.mods.halloween.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;


public class ModConfiguration
{

	private static class DefaultValues
	{
		private static final boolean ambientSoundsEnabled = true;
		private static final int ambientSoundInterval = 900;
	}

	private static Configuration config = null;

	public static final String CATEGORY_SOUNDS = "sounds";

	public static boolean ambientSoundsEnabled = DefaultValues.ambientSoundsEnabled;
	public static int ambientSoundInterval = DefaultValues.ambientSoundInterval;


	public static void initializeConfiguration()
	{
		// load the configuration from the file
		config = new Configuration(new File(Loader.instance().getConfigDir(), HalloweenMod.MODID + ".cfg"));
		config.load();

		// get the configuration properties
		config.addCustomCategoryComment(ModConfiguration.CATEGORY_SOUNDS, "");
		Property propAmbientSoundsEnabled = config.get(ModConfiguration.CATEGORY_SOUNDS, "ambientSoundsEnabled", DefaultValues.ambientSoundsEnabled, "Play ambient sound effects.\nDefault is " + DefaultValues.ambientSoundsEnabled + ".");
		Property propAmbientSoundInterval = config.get(ModConfiguration.CATEGORY_SOUNDS, "ambientSoundInterval", DefaultValues.ambientSoundInterval, "Frequency with which ambient sound effects are played.\nDefault is " + DefaultValues.ambientSoundInterval + " ticks.");

		// set the category property order
		List<String> propOrderSounds = new ArrayList<String>();
		propOrderSounds.add(propAmbientSoundsEnabled.getName());
		propOrderSounds.add(propAmbientSoundInterval.getName());
		config.setCategoryPropertyOrder(ModConfiguration.CATEGORY_SOUNDS, propOrderSounds);

		// read the property values
		ambientSoundsEnabled = propAmbientSoundsEnabled.getBoolean();
		ambientSoundInterval = propAmbientSoundInterval.getInt();

		// save the configuration to the file
		if (config.hasChanged()) config.save();
	}

}