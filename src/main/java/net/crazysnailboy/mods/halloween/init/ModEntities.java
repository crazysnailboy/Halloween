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
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSkeleton;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeSpider;
import net.crazysnailboy.mods.halloween.entity.monster.fake.EntityFakeZombie;
import net.crazysnailboy.mods.halloween.entity.passive.EntityTreater;
import net.crazysnailboy.mods.halloween.entity.projectile.EntityCurseOrb;
import net.crazysnailboy.mods.halloween.entity.projectile.fake.EntityFakeArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
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


	public static void registerEntities()
	{
		// hostile mobs
		registerEntity(EntityCreeperween.class, "Creeperween", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityJumpkin.class, "Jumpkin", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityZombieHands.class, "ZombieHands", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityHallowitch.class, "Hallowitch", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityHaunter.class, "Haunter", 64, 3, true, 0x000000, 0xFFFFFF);
		// fake hostile mobs
		registerEntity(EntityFakeCreeper.class, "FakeCreeper", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityFakeSkeleton.class, "FakeSkeleton", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityFakeSpider.class, "FakeSpider", 64, 3, true, 0x000000, 0xFFFFFF);
		registerEntity(EntityFakeZombie.class, "FakeZombie", 64, 3, true, 0x000000, 0xFFFFFF);

		// passive mobs
		registerEntity(EntityTreater.class, "Treater", 64, 3, true, 0x000000, 0xFFFFFF);

		// projectiles
		registerEntity(EntityCurseOrb.class, "CurseOrb", 64, 10, true);
		// fake projectiles
		registerEntity(EntityFakeArrow.class, "FakeArrow", 64, 20, true);

		// effects
		registerEntity(EntityCreeperCurse.class, "CreeperCurse", 64, 3, true);
		registerEntity(EntityGhastCurse.class, "GhastCurse", 64, 3, true);
		registerEntity(EntitySkeletonCurse.class, "SkeletonCurse", 64, 3, true);
		registerEntity(EntitySlimeCurse.class, "SlimeCurse", 64, 3, true);
		registerEntity(EntitySpiderCurse.class, "SpiderCurse", 64, 3, true);
		registerEntity(EntityZombieCurse.class, "ZombieCurse", 64, 3, true);


	}

	public static void addEntitySpawns()
	{
		Biome[] biomes = getBiomeList(EntityCreeper.class, EnumCreatureType.MONSTER);

		EntityRegistry.addSpawn(EntityJumpkin.class, 100, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityZombieHands.class, 100, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityHallowitch.class, 75, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityCreeperween.class, 75, 4, 4, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityHaunter.class, 10, 1, 1, EnumCreatureType.MONSTER, biomes);
		EntityRegistry.addSpawn(EntityTreater.class, 50, 4, 4, EnumCreatureType.MONSTER, biomes);

//		copySpawns(EntityCreeperween.class, EnumCreatureType.MONSTER, EntityCreeper.class, EnumCreatureType.MONSTER);
//		copySpawns(EntityJumpkin.class, EnumCreatureType.MONSTER, EntityCreeper.class, EnumCreatureType.MONSTER);
//		copySpawns(EntityZombieHands.class, EnumCreatureType.MONSTER, EntityZombie.class, EnumCreatureType.MONSTER);
//		copySpawns(EntityHallowitch.class, EnumCreatureType.MONSTER, EntityWitch.class, EnumCreatureType.MONSTER);
//		copySpawns(EntityHaunter.class, EnumCreatureType.MONSTER, EntityCreeper.class, EnumCreatureType.MONSTER);
	}





	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, entityName, id++, HalloweenMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(entityClass, entityName, id++, HalloweenMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
	}


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


//	private static void copySpawns(final Class<? extends EntityLiving> classToAdd, final EnumCreatureType creatureTypeToAdd, final Class<? extends EntityLiving> classToCopy, final EnumCreatureType creatureTypeToCopy)
//	{
//		for (final Biome biome : ForgeRegistries.BIOMES)
//		{
//			biome.getSpawnableList(creatureTypeToCopy).stream().filter(new Predicate<SpawnListEntry>()
//			{
//				@Override
//				public boolean test(SpawnListEntry entry)
//				{
//					return entry.entityClass == classToCopy;
//				}
//			})
//			.findFirst()
//			.ifPresent(new Consumer<SpawnListEntry>()
//			{
//				@Override
//				public void accept(SpawnListEntry spawnListEntry)
//				{
//					biome.getSpawnableList(creatureTypeToAdd).add(new Biome.SpawnListEntry(classToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount));
//				}
//			});
//		}
//	}





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

		// projectiles
		registerEntityRenderingHandler(EntityCurseOrb.class, RenderCurseOrb.class);
		// fake projectiles
		registerEntityRenderingHandler(EntityFakeArrow.class, RenderFakeArrow.class);

		// effects
		registerEntityRenderingHandler(EntityCurse.class, RenderCurse.class);
	}


	@SideOnly(Side.CLIENT)
	private static <T extends Entity> void registerEntityRenderingHandler(Class<T> entityClass, Class<? extends Render<? super T>> renderClass)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderFactory.create(renderClass));
	}

}
