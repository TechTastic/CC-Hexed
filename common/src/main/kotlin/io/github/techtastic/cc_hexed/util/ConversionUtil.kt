package io.github.techtastic.cc_hexed.util

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.shared.util.NBTUtil
import dev.architectury.platform.Platform
import io.github.techtastic.cc_hexed.interop.MoreIotasInterop
import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.ShortTag
import net.minecraft.nbt.StringTag
import net.minecraft.server.level.ServerLevel
import kotlin.collections.toList

object ConversionUtil {
    @JvmStatic
    fun Iota.toLua(): Any? {
        if (Platform.isModLoaded("moreiotas"))
            MoreIotasInterop.toLua(this)?.let { return it }
        return when(this) {
            is NullIota -> null
            is BooleanIota -> this.bool
            is DoubleIota -> this.double
            is ListIota -> this.list.map { it.toLua() }
            else -> NBTUtil.toLua(IotaType.serialize(this))
        }
    }

    @JvmStatic
    fun Any?.toIota(level: ServerLevel): Iota {
        println("Object to Convert: $this")
        return when (this) {
            is Number -> DoubleIota(this.toDouble())
            is Boolean -> BooleanIota(this)
            is String -> if (Platform.isModLoaded("moreiotas")) MoreIotasInterop.toIota(this)!! else throw LuaException("MoreIotas is needed to handle string!")
            is HashMap<*,*> -> this.toIota(level)
            null -> NullIota()
            else -> GarbageIota()
        }
    }

    @JvmStatic
    fun HashMap<*,*>.toIota(level: ServerLevel): Iota {
        if (this.contains(HexIotaTypes.KEY_TYPE) && this[HexIotaTypes.KEY_TYPE] is String && this.containsKey(HexIotaTypes.KEY_DATA) && this[HexIotaTypes.KEY_DATA] is Map<*,*>) { println("NBT to deserialize: $this")
            val tag = CompoundTag()
            tag.putString(HexIotaTypes.KEY_TYPE, this[HexIotaTypes.KEY_TYPE] as String)
            tag.put(HexIotaTypes.KEY_DATA, (this[HexIotaTypes.KEY_DATA] as Map<*,*>).toNBT())
            return IotaType.deserialize(tag, level) ?: GarbageIota()
        }

        this.toArrayList()?.let { list -> return ListIota(list.map { any -> any.toIota(level) }) }

        // TODO: Map or Json Handling

        return GarbageIota()
    }

    @JvmStatic
    inline fun <reified T: Number> Map<*, *>.toNumericArray(num: Class<T>): List<T>? {
        if (!this.values.all { value -> value?.javaClass == num }) return null
        return this.values.map(num::cast).toList()
    }

    @JvmStatic
    inline fun <reified T: Number> List<*>.toNumericArray(num: Class<T>): List<T>? {
        if (!this.all { value -> value?.javaClass == num }) return null
        return this.map(num::cast).toList()
    }

    @JvmStatic
    fun Map<*,*>.toArrayList(): List<*>? {
        if (this.keys.all { key ->
            key is Number && ((key !is Double && key !is Float) || (key is Double && key.isFinite() && key == key.toLong().toDouble()) || (key is Double && key.isFinite() && key == key.toLong().toDouble()))
        })
            return (this.filterKeys { key -> key is Number }).toSortedMap(compareBy { key -> (key as Number).toLong() }).values.toList()
        return null
    }

    @JvmStatic
    fun Map<*,*>.toNBT(): CompoundTag {
        val nbt = CompoundTag()

        this.forEach { (key, value) ->
            val keyStr = key.toString()

            when (value) {
                is String -> nbt.putString(keyStr, value)
                is Double -> nbt.putDouble(keyStr, value)
                is Boolean -> nbt.putBoolean(keyStr, value)
                is Int -> nbt.putInt(keyStr, value)
                is Long -> nbt.putLong(keyStr, value)
                is Float -> nbt.putFloat(keyStr, value)
                is List<*> -> {
                    value.toNumericArray(Byte::class.java)?.let { bytes -> nbt.putByteArray(keyStr, bytes) } ?:
                    value.toNumericArray(Int::class.java)?.let { ints -> nbt.putIntArray(keyStr, ints) } ?:
                    value.toNumericArray(Long::class.java)?.let { longs -> nbt.putLongArray(keyStr, longs) } ?:
                    nbt.putList(keyStr, value.toListTag())
                }
                is Map<*, *> -> {
                    value.toArrayList()?.let { list ->
                        nbt.putList(keyStr, list.toListTag())
                        return@forEach
                    } ?:
                    value.toNumericArray(Byte::class.java)?.let { bytes -> nbt.putByteArray(keyStr, bytes) } ?:
                    value.toNumericArray(Int::class.java)?.let { ints -> nbt.putIntArray(keyStr, ints) } ?:
                    value.toNumericArray(Long::class.java)?.let { longs -> nbt.putLongArray(keyStr, longs) } ?:
                    nbt.put(keyStr, value.toNBT())
                }
                null -> {}
                else -> throw LuaException("Unsupported type: ${value::class.simpleName}")
            }
        }

        return nbt
    }

    @JvmStatic
    fun List<*>.toListTag(): ListTag {
        val nbtList = ListTag()

        this.forEach { value ->
            when (value) {
                null -> return@forEach
                is String -> nbtList.add(StringTag.valueOf(value))
                is Byte -> nbtList.add(ByteTag.valueOf(value))
                is Short -> nbtList.add(ShortTag.valueOf(value))
                is Int -> nbtList.add(IntTag.valueOf(value))
                is Float -> nbtList.add(FloatTag.valueOf(value))
                is Double -> nbtList.add(DoubleTag.valueOf(value))
                is Long -> nbtList.add(LongTag.valueOf(value))
                is Map<*, *> -> {
                    value.toArrayList()?.toListTag()?.let { list ->
                        nbtList.add(list)
                        return@forEach
                    } ?:
                    value.toNumericArray(Int::class.java)?.let { ints -> nbtList.add(IntArrayTag(ints)) } ?:
                    value.toNumericArray(Long::class.java)?.let { longs -> nbtList.add(LongArrayTag(longs)) } ?:
                    nbtList.add(value.toNBT())
                }
                else -> throw LuaException("Unsupported type: ${value::class.simpleName}")
            }
        }

        return nbtList
    }
}