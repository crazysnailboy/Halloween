package net.crazysnailboy.mods.halloween.common.network;

import io.netty.buffer.ByteBuf;
import net.crazysnailboy.mods.halloween.common.config.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigSyncMessage implements IMessage, IMessageHandler<ConfigSyncMessage, IMessage>
{

	private NBTTagCompound compound = null;

	public ConfigSyncMessage()
	{
	}

	public ConfigSyncMessage(NBTTagCompound compound)
	{
		this.compound = compound;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, compound);
	}

	@Override
	public IMessage onMessage(final ConfigSyncMessage message, MessageContext ctx)
	{
		IThreadListener threadListener = (ctx.side == Side.SERVER ? (WorldServer)ctx.getServerHandler().player.world : Minecraft.getMinecraft());
		threadListener.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ModConfiguration.readFromNBT(message.compound);
			}
		});
		return null;
	}

}
