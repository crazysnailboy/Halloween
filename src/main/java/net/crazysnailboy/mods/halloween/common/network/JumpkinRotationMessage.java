package net.crazysnailboy.mods.halloween.common.network;

import io.netty.buffer.ByteBuf;
import net.crazysnailboy.mods.halloween.entity.monster.EntityJumpkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class JumpkinRotationMessage implements IMessage, IMessageHandler<JumpkinRotationMessage, IMessage>
{

	private int entityId;
	private float rotationYaw;
	private float rotationPitch;


	public JumpkinRotationMessage()
	{
	}

	public JumpkinRotationMessage(EntityJumpkin entity)
	{
		this.entityId = entity.getEntityId();
		this.rotationYaw = entity.rotationYaw;
		this.rotationPitch = entity.rotationPitch;
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityId = ByteBufUtils.readVarInt(buf, 4);
		this.rotationYaw = buf.readFloat();
		this.rotationPitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, this.entityId, 4);
		buf.writeFloat(this.rotationYaw);
		buf.writeFloat(this.rotationPitch);
	}


	@Override
	public IMessage onMessage(final JumpkinRotationMessage message, MessageContext ctx)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		final WorldClient world = minecraft.world;

		minecraft.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityJumpkin entity = (EntityJumpkin)world.getEntityByID(message.entityId);
				entity.rotationYaw = message.rotationYaw;
				entity.rotationPitch = message.rotationPitch;
			}
		});
		return null;
	}

}