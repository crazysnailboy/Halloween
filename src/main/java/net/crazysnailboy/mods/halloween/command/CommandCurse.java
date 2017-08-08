package net.crazysnailboy.mods.halloween.command;

import java.util.Collections;
import java.util.List;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCreeperCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse.EnumCurseType;
import net.crazysnailboy.mods.halloween.entity.effect.EntityGhastCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySkeletonCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySlimeCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySpiderCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityZombieCurse;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;


public class CommandCurse extends CommandBase
{

	@Override
	public String getName()
	{
		return "curse";
	}


	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		World world = sender.getEntityWorld();
		if (!world.isRemote && args.length > 0)
		{
			EntityPlayer player = this.getCommandSenderAsPlayer(sender);
			EntityCurse curse = null;

			EnumCurseType curseType = EnumCurseType.byName(args[0]);
			switch(curseType)
			{
				case CREEPER: curse = new EntityCreeperCurse(world, player); break;
				case GHAST: curse = new EntityGhastCurse(world, player); break;
				case SKELETON: curse = new EntitySkeletonCurse(world, player); break;
				case SLIME: curse = new EntitySlimeCurse(world, player); break;
				case SPIDER: curse = new EntitySpiderCurse(world, player); break;
				case ZOMBIE: curse = new EntityZombieCurse(world, player); break;
			}
			world.spawnEntity(curse);
		}
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/curse";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList(this.getName());
	}

}