package net.crazysnailboy.mods.halloween.network.datasync;

import java.io.IOException;
import net.crazysnailboy.mods.halloween.entity.monster.EntityHaunter.EnumTransparencyState;
import net.crazysnailboy.mods.halloween.entity.monster.EntityZombieHands;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;


public class ModDataSerializers
{

	public static class EnumDataSerializer<T extends Enum> implements DataSerializer<T>
	{
		private final Class<T> enumClass;

		public EnumDataSerializer(Class<T> enumClass)
		{
			this.enumClass = enumClass;
		}

		@Override
		public void write(PacketBuffer buf, T value)
		{
			buf.writeEnumValue(value);
		}

		@Override
		public T read(PacketBuffer buf) throws IOException
		{
			return (T)buf.readEnumValue(enumClass);
		}

		@Override
		public DataParameter<T> createKey(int id)
		{
			return new DataParameter(id, this);
		}

		@Override
		public T copyValue(T value)
		{
			return value;
		}
	}


	public static final DataSerializer<EnumTransparencyState> HAUNTER_TRANSPARENCY = new EnumDataSerializer<EnumTransparencyState>(EnumTransparencyState.class);
	public static final DataSerializer<EntityZombieHands.ZombieType> ZOMBIE_TYPE = new EnumDataSerializer<EntityZombieHands.ZombieType>(EntityZombieHands.ZombieType.class);


	static
	{
		DataSerializers.registerSerializer(HAUNTER_TRANSPARENCY);
		DataSerializers.registerSerializer(ZOMBIE_TYPE);
	}

}
