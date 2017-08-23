package net.crazysnailboy.mods.halloween.client.renderer.entity;

import net.crazysnailboy.mods.halloween.client.model.ModelZombieHands;
import net.crazysnailboy.mods.halloween.entity.monster.EntityZombieHands;
import net.crazysnailboy.mods.halloween.entity.monster.ZombieType;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderZombieHands extends RenderBiped<EntityZombieHands>
{

	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");


	public RenderZombieHands(RenderManager renderManager)
	{
		super(renderManager, new ModelZombieHands(), 0.3F);
	}


	@Override
	protected void preRenderCallback(EntityZombieHands entity, float partialTickTime)
	{
		super.preRenderCallback(entity, partialTickTime);
	}

	@Override
	public void doRender(EntityZombieHands entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombieHands entity)
	{
		return entity.getZombieType() == ZombieType.HUSK ? HUSK_ZOMBIE_TEXTURES : ZOMBIE_TEXTURES;
	}

}