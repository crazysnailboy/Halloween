package net.crazysnailboy.mods.halloween.entity.passive;

import java.util.Random;
import javax.annotation.Nullable;
import net.crazysnailboy.mods.halloween.entity.ai.EntityAITreater;
import net.crazysnailboy.mods.halloween.init.ModItems;
import net.crazysnailboy.mods.halloween.item.ItemCandy.EnumCandyFlavour;
import net.crazysnailboy.mods.halloween.util.BlockUtils;
import net.crazysnailboy.mods.halloween.util.EntityUtils;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;


/**
 * Because children are basically animals, right?
 *
 */
public class EntityTreater extends EntityAnimal
{

	private static final DataParameter<Integer> TREATER_TYPE = EntityDataManager.<Integer>createKey(EntityTreater.class, DataSerializers.VARINT);


	public EntityTreater(World world)
	{
		this(world, new Random().nextInt(5));
	}

	public EntityTreater(World world, int treaterType)
	{
		super(world);
		this.setTreaterType(treaterType);
		this.setSize(0.5F, 1.5F);
		this.setHealth(10.0F);
		this.setCanPickUpLoot(true);
	}


	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(TREATER_TYPE, Integer.valueOf(0));
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAITempt(this, 0.6D, ModItems.MEGA_CANDY, false));
		this.tasks.addTask(4, new EntityAITreater.MoveToNearestCandy(this, 0.5D));
		this.tasks.addTask(5, new EntityAIWander(this, 0.5D));
		this.tasks.addTask(6, new EntityAITreater.WatchClosestPlayer(this, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			// make treaters despawn when it becomes daytime
			if (this.world.isDaytime())
			{
				this.setDead();
				this.spawnExplosionParticle();
			}
		}
		super.onLivingUpdate();
	}

	/**
	 * If the player tries to give the treater candy, they will accept it if it's their favourite type, or if it's mega candy.
	 * The treater will respond to the player with a chat message, and if the candy is accepted will spawn heart particles and consume the candy.
	 */
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack)
	{
		if (stack != null)
		{
			boolean consumeItem = false;

			// if the stack contains candy...
			if (stack.getItem() == ModItems.CANDY)
			{
				// if the stack contains the treater's favourite type of candy...
				if (stack.getMetadata() == this.getTreaterType().getCandyType().ordinal())
				{
					this.chatItUp(player, EnumTreaterMessage.THANKING);
					consumeItem = true;
					dropThankItem(player);
				}
				else
				{
					this.chatItUp(player, EnumTreaterMessage.MISTAKING);
				}
			}

			if (stack.getItem() == ModItems.MEGA_CANDY)
			{
				this.chatItUp(player, EnumTreaterMessage.SUPERING);
				consumeItem = true;
			}

			if (consumeItem)
			{
				this.consumeItemFromStack(player, stack);
				this.world.setEntityState(this, (byte)18); // spawns heart particles
			}
		}

		return false; // EntityAgeable#processInteract only returns true if the item is a spawn egg
	}

	/**
	 * When a treater is hurt by a player,
	 * The treater will send a chat message to that player
	 */
	@Override
	protected void damageEntity(DamageSource damageSource, float damageAmount)
	{
		super.damageEntity(damageSource, damageAmount);
		if (damageSource.getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)damageSource.getSourceOfDamage();
			this.chatItUp(player, EnumTreaterMessage.HURTING);
		}
	}

	/**
	 * When a treater is killed by a player,
	 * a mob of the same type (i.e. of the costume the treater is wearing) will spawn in its place.
	 */
	@Override
	public void onDeath(DamageSource damageSource)
	{
		if (damageSource.getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityMob mob = null;
			switch (this.getTreaterType())
			{
				case CREEPER: mob = new EntityCreeper(this.world); break;
				case SKELETON: mob = new EntitySkeleton(this.world); break;
				case SPIDER: mob = new EntitySpider(this.world); break;
				case WITCH: mob = new EntityWitch(this.world); break;
				case ZOMBIE: mob = new EntityZombie(this.world); break;
			}
			mob.setPosition(this.posX, this.posY, this.posZ);
			this.world.spawnEntity(mob);
		}
		super.onDeath(damageSource);
	}

	/**
	 * Used to make treaters pick up items.
	 */
	@Override
	protected void updateEquipmentIfNeeded(EntityItem entity)
	{
		ItemStack stack = entity.getEntityItem();

		if (stack.getItem() == ModItems.MEGA_CANDY || (stack.getItem() == ModItems.CANDY && stack.getMetadata() == this.getTreaterType().getCandyType().getMetadata()))
		{
			String thrower = entity.getThrower();
			if (!StringUtils.isNullOrEmpty(thrower))
			{
				EntityPlayer player = this.world.getPlayerEntityByName(thrower);
				if (player != null)
				{
					if (stack.getItem() == ModItems.MEGA_CANDY)
					{
						this.chatItUp(player, EnumTreaterMessage.SUPERING);
					}
					else
					{
						this.chatItUp(player, EnumTreaterMessage.THANKING);
						this.dropThankItem(player);
					}

				}
			}
			entity.setDead();
		}
	}

	@Override
	public boolean getCanSpawnHere()
	{
		BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
		if (this.world.getLight(pos) > 0 && BlockUtils.isSoftGround(this.world, pos.down()))
		{
			return EntityUtils.getCanMobSpawnHere(this);
		}
		return false;
	}

	/**
	 * Required for all animals - overrides the abstract method {@link EntityAgeable#createChild(EntityAgeable)}.
	 * Returns null because we don't want our treaters having children!
	 */
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}

	/**
	 * Overrides {@link EntityAnimal#isBreedingItem(ItemStack)}, which returns true if the item is wheat.
	 * Returns false because we don't want our treaters having children!
	 */
	@Override
	public boolean isBreedingItem(@Nullable ItemStack stack)
	{
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("TreaterType", this.dataManager.get(TREATER_TYPE).intValue());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setTreaterType(compound.getInteger("TreaterType"));
		this.setCanPickUpLoot(true); // EntityVillager sets this to true here as well
	}


	public EnumTreaterType getTreaterType()
	{
		return EnumTreaterType.values()[this.dataManager.get(TREATER_TYPE).intValue()];
	}

	private void setTreaterType(int value)
	{
		this.dataManager.set(TREATER_TYPE, value);
	}

	private void setTreaterType(EnumTreaterType value)
	{
		this.dataManager.set(TREATER_TYPE, value.ordinal());
	}

	/**
	 * Drop items from the loot table of the entity this Treater is costumed as.
	 * Used to give "thankyou" items to players who give Treaters candy.
	 */
	private void dropThankItem(EntityPlayer player)
	{
		if (!this.world.isRemote)
		{
			LootTable lootTable = this.world.getLootTableManager().getLootTableFromLocation(this.getTreaterType().getLootTable());
			LootContext.Builder builder = new LootContext.Builder((WorldServer)this.world)
				.withLootedEntity(this).withDamageSource(DamageSource.generic).withPlayer(player).withLuck(player.getLuck());

			for (ItemStack stack : lootTable.generateLootForPools(this.rand, builder.build()))
			{
				InventoryHelper.spawnItemStack(this.world, this.posX, this.posY, this.posZ, stack);
			}
		}
	}

	/**
	 * Sends a chat message to the target player.
	 * The message sent is chosen at random from those available for the message type, and where appropriate the treater type.
	 */
	public void chatItUp(EntityPlayer player, EnumTreaterMessage messageType)
	{
		player.sendMessage(new TextComponentTranslation(messageType.getTranslationKey(this.getTreaterType())));
	}


	public enum EnumTreaterMessage
	{
		GREETING("greet", 3, true),
		MISTAKING("mistake", 2, true),
		THANKING("thank", 2, true),
		HURTING("hurt", 3, false),
		SUPERING("mega", 3, false);

		private final String messageType;
		private final int messageVariants;
		private final boolean hasTypeVariant;

		private EnumTreaterMessage(String messageType, int messageVariants, boolean hasTypeVariant)
		{
			this.messageType = messageType;
			this.messageVariants = messageVariants;
			this.hasTypeVariant = hasTypeVariant;
		}

		public String getTranslationKey(EnumTreaterType treaterType)
		{
			int messageVariant = (new Random().nextInt(this.messageVariants) + 1);
			if (this.hasTypeVariant)
			{
				return String.format("chat.treater.%1$s.%2$s.%3$d", this.messageType, treaterType.getTypeName(), messageVariant);
			}
			else
			{
				return String.format("chat.treater.%1$s.%2$d", this.messageType, messageVariant);
			}
		}
	}

	public enum EnumTreaterType
	{
		CREEPER("creeper", EnumCandyFlavour.APPLE, LootTableList.ENTITIES_CREEPER),
		SKELETON("skeleton", EnumCandyFlavour.LEMON, LootTableList.ENTITIES_SKELETON),
		SPIDER("spider", EnumCandyFlavour.PUMPKIN, LootTableList.ENTITIES_SPIDER),
		WITCH("witch", EnumCandyFlavour.GRAPE, LootTableList.ENTITIES_WITCH),
		ZOMBIE("zombie", EnumCandyFlavour.WATERMELON, LootTableList.ENTITIES_ZOMBIE);

		private final String typeName;
		private final EnumCandyFlavour candyType;
		private final ResourceLocation lootTable;


		private EnumTreaterType(String typeName, EnumCandyFlavour candyType, ResourceLocation lootTable)
		{
			this.typeName = typeName;
			this.candyType = candyType;
			this.lootTable = lootTable;
		}


		public String getTypeName()
		{
			return this.typeName;
		}

		public EnumCandyFlavour getCandyType()
		{
			return this.candyType;
		}

		public ResourceLocation getLootTable()
		{
			return this.lootTable;
		}


		public static EnumTreaterType getRandom()
		{
			return values()[new Random().nextInt(values().length)];
		}

	}


}
