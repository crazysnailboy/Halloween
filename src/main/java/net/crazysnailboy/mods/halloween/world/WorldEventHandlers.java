package net.crazysnailboy.mods.halloween.world;

import java.util.Random;
import net.crazysnailboy.mods.halloween.init.ModSoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServerMulti;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;


@EventBusSubscriber
public class WorldEventHandlers
{

	private static final Random rand = new Random();
	private static int ambienceTicks = 900 + rand.nextInt(150);


	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event)
	{
		if (event.side == Side.SERVER && event.phase == Phase.END && !(event.world instanceof WorldServerMulti))
		{
			World world = event.world;
			if (!world.isDaytime())
			{
				if (ambienceTicks > 0)
				{
					ambienceTicks--;
					return;
				}

				SoundEvent soundEvent = null;
				switch (world.rand.nextInt(3))
				{
					case 0:
						soundEvent = ModSoundEvents.AMBIENT_HALLOWEEN;
						break;
					case 1:
						soundEvent = ModSoundEvents.AMBIENT_OOO;
						break;
					case 2:
						soundEvent = ModSoundEvents.AMBIENT_SPOOKY;
						break;
				}

				for (EntityPlayer player : world.playerEntities)
				{
					world.playSound(null, player.getPosition(), soundEvent, SoundCategory.AMBIENT, 4.0F, 1.0F);
					world.playSound(null, player.getPosition(), SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 4.0F, 1.0F);
				}

				ambienceTicks = 900 + rand.nextInt(150);

			}
		}
	}

}
