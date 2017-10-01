package net.crazysnailboy.mods.halloween;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.crazysnailboy.mods.halloween.command.CommandCurse;
import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.crazysnailboy.mods.halloween.common.network.ConfigSyncMessage;
import net.crazysnailboy.mods.halloween.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(modid = HalloweenMod.MODID, name = HalloweenMod.NAME, version = HalloweenMod.VERSION, updateJSON = HalloweenMod.UPDATEJSON)
public class HalloweenMod
{

	public static final String MODID = "halloween";
	public static final String NAME = "No-Holds-Barred Halloween Mod";
	public static final String VERSION = "${version}";
	public static final String UPDATEJSON = "https://raw.githubusercontent.com/crazysnailboy/Halloween/master/update.json";

	private static final String CLIENT_PROXY_CLASS = "net.crazysnailboy.mods.halloween.proxy.ClientProxy";
	private static final String SERVER_PROXY_CLASS = "net.crazysnailboy.mods.halloween.proxy.CommonProxy";

	@Instance(MODID)
	public static HalloweenMod instance;

	@SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> handle;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfiguration.initializeConfiguration();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandCurse());
	}

	@EventHandler
	public static void onServerStarted(FMLServerStartedEvent event)
	{
		if (ModConfiguration.halloweenCheckInterval > 0)
		{
			handle = scheduler.scheduleAtFixedRate(new Runnable()
			{
				@Override
				public void run()
				{
					boolean isHalloween = ModConfiguration.isHalloween();
					if (isHalloween != ModConfiguration.isHalloween)
					{
						ModConfiguration.isHalloween = isHalloween;
						NBTTagCompound compound = ModConfiguration.writeToNBT(new NBTTagCompound());
						HalloweenMod.NETWORK.sendToAll(new ConfigSyncMessage(compound));
					}
				}
			},
			0, ModConfiguration.halloweenCheckInterval, TimeUnit.MINUTES);
		}
	}

	@EventHandler
	public static void onServerStopped(FMLServerStoppedEvent event)
	{
		if (ModConfiguration.halloweenCheckInterval > 0)
		{
			// stop the scheduler
			scheduler.schedule(new Runnable()
			{
				@Override
				public void run()
				{
					handle.cancel(true);
				}

			}, 0, TimeUnit.SECONDS);
		}
	}

}