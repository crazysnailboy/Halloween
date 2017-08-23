package net.crazysnailboy.mods.halloween.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.crazysnailboy.mods.halloween.HalloweenMod;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderCreeperween;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderCurse;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderCurseOrb;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderFakeArrow;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderHallowitch;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderHaunter;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderJumpkin;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderTreater;
import net.crazysnailboy.mods.halloween.client.renderer.entity.RenderZombieHands;
import net.crazysnailboy.mods.halloween.client.renderer.entity.factory.RenderFactory;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCreeperCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityGhastCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySkeletonCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySlimeCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntitySpiderCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityZombieCurse;
import net.crazysnailboy.mods.halloween.entity.monster.EntityCreeperween;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHallowitch;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter;
import net.crazysnailboy.mods.halloween.entity.monster.EntityJumpkin;
import net.crazysnailboy.mods.halloween.entity.monster.EntityZombieHands;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeCreeper;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeHusk;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSkeleton;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSpider;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeStray;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeZombie;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.crazysnailboy.mods.halloween.entity.projectile.EntityCurseOrb;
import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeArrow;
import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeTippedArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ModEntities
{

	private static int id = 0;


	/**
	 * Register all the mod's entities with the Entity Registry.
	 */
	public static void registerEntities()
	{
		// hostile mobs
		registerLiving(EntityCreeperween.class, "creeperween", 0x000000, 0xFFFFFF);
		registerLiving(EntityJumpkin.class, "jumpkin", 0x000000, 0xFFFFFF);
		registerLiving(EntityZombieHands.class, "zombiehands", 0x000000, 0xFFFFFF);
		registerLiving(EntityHallowitch.class, "hallowitch", 0x000000, 0xFFFFFF);
		registerLiving(EntityHaunter.class, "haunter", 0x000000, 0xFFFFFF);
		// fake hostile mobs
		registerLiving(EntityFakeCreeper.class, "fakecreeper", 0x000000, 0xFFFFFF);
		registerLiving(EntityFakeHusk.class, "fakehusk", 0x000000, 0xFFFFFF);
		registerLiving(EntityFakeSkeleton.class, "fakeskeleton", 0x000000, 0xFFFFFF);
		registerLiving(EntityFakeSpider.class, "fakespider", 0x000000, 0xFFFFFF);
		registerLiving(EntityFakeStray.class, "fakestray", 0x000000, 0xFFFFFF);
		registerLiving(EntityFakeZombie.class, "fakezombie", 0x000000, 0xFFFFFF);
		// passive mobs
		registerLiving(EntityTreater.class, "treater", 0x000000, 0xFFFFFF);

		// throwables
		registerThrowable(EntityCurseOrb.class, "curseorb");
		// fake projectiles
		registerProjectile(EntityFakeArrow.class, "fakearrow");
		registerProjectile(EntityFakeTippedArrow.class, "faketippedarrow");

		// effects
		registerEntity(EntityCreeperCurse.class, "creepercurse", 64, 3, true);
		registerEntity(EntityGhastCurse.class, "ghastcurse", 64, 3, true);
		registerEntity(EntitySkeletonCurse.class, "skeletoncurse", 64, 3, true);
		registerEntity(EntitySlimeCurse.class, "slimecurse", 64, 3, true);
		registerEntity(EntitySpiderCurse.class, "spidercurse", 64, 3, true);
		registerEntity(EntityZombieCurse.class, "zombiecurse", 64, 3, true);
	}

	private static void registerLiving(Class<? extends Entity> entityClass, String entityName, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(HalloweenMod.MODID, entityName), entityClass, entityName, id++, HalloweenMod.instance, 64, 3, true, eggPrimary, eggSecondary);
	}

	private static void registerThrowable(Class<? extends Entity> entityClass, String entityName)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(HalloweenMod.MODID, entityName), entityClass, entityName, id++, HalloweenMod.instance, 64, 10, true);
	}

	private static void registerProjectile(Class<? extends Entity> entityClass, String entityName)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(HalloweenMod.MODID, entityName), entityClass, entityName, id++, HalloweenMod.instance, 64, 20, true);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(HalloweenMod.MODID, entityName), entityClass, entityName, id++, HalloweenMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}


	/**
	 * Add spawning rules for the living mobs we want to be able to spawn naturally.
	 */
	public static void addEntitySpawns()
	{
		// get the list of biomes that creepers can spawn in, we'll use the same biomes for all of our mobs
		Biome[] biomes = getBiomeList(EntityCreeper.class, EnumCreatureType.MONSTER);

		// add the spawn rules to the entity registry
		EntityRegistry.addSpawn(EntityJumpkin.class, 100, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityZombieHands.class, 100, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityHallowitch.class, 75, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityCreeperween.class, 75, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityHaunter.class, 5, 1, 1, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityTreater.class, 50, 4, 4, EnumCreatureType.MONSTER, biomes);
	}

	/**
	 * Helper method to return an array of biomes in which an already existing instance of EntityLiving can spawn.
	 */
	private static final Biome[] getBiomeList(final Class<? extends EntityLiving> classToCopy, final EnumCreatureType creatureTypeToCopy)
	{
		final List<Biome> biomes = new ArrayList<Biome>();

		for (final Biome biome : ForgeRegistries.BIOMES)
		{
			biome.getSpawnableList(creatureTypeToCopy).stream().filter(new Predicate<SpawnListEntry>()
			{
				@Override
				public boolean test(SpawnListEntry entry)
				{
					return entry.entityClass == classToCopy;
				}
			})
			.findFirst()
			.ifPresent(new Consumer<SpawnListEntry>()
			{
				@Override
				public void accept(SpawnListEntry spawnListEntry)
				{
					biomes.add(biome);
				}
			});
		}

		return biomes.toArray(new Biome[biomes.size()]);
	}


	/**
	 * Register the rendering handlers for the mod's entities.
	 */
	@SideOnly(Side.CLIENT)
	public static void registerRenderingHandlers()
	{
		// hostile mobs
		registerEntityRenderingHandler(EntityCreeperween.class, RenderCreeperween.class);
		registerEntityRenderingHandler(EntityJumpkin.class, RenderJumpkin.class);
		registerEntityRenderingHandler(EntityZombieHands.class, RenderZombieHands.class);
		registerEntityRenderingHandler(EntityHallowitch.class, RenderHallowitch.class);
		registerEntityRenderingHandler(EntityHaunter.class, RenderHaunter.class);
		// passive mobs
		registerEntityRenderingHandler(EntityTreater.class, RenderTreater.class);

		// throwables
		registerEntityRenderingHandler(EntityCurseOrb.class, RenderCurseOrb.class);
		// fake projectiles
		registerEntityRenderingHandler(EntityFakeArrow.class, RenderFakeArrow.class);
		registerEntityRenderingHandler(EntityFakeTippedArrow.class, RenderTippedArrow.class);

		// effects
		registerEntityRenderingHandler(EntityCurse.class, RenderCurse.class);
	}

	@SideOnly(Side.CLIENT)
	private static <T extends Entity> void registerEntityRenderingHandler(Class<T> entityClass, Class<? extends Render<? super T>> renderClass)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderFactory.create(renderClass));
	}

}