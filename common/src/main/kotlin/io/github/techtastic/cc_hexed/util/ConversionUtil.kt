package io.github.techtastic.cc_hexed.util

import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import dan200.computercraft.shared.util.NBTUtil
import net.minecraft.server.level.ServerLevel

object ConversionUtil {
    @JvmStatic
    fun Iota.toLua(): Any? {
        return when(this) {
            is NullIota -> null
            is BooleanIota -> this.bool
            is DoubleIota -> this.double
            is ListIota -> this.list.map { it.toLua() }
            else -> NBTUtil.toLua(this.serialize())
        }
    }

    @JvmStatic
    fun Any?.toIota(level: ServerLevel): Iota {
        if (this == null) return NullIota()
        return when (this) {
            is Number -> DoubleIota(this.toDouble())
            is Boolean -> BooleanIota(this)
            is HashMap<*,*> -> this.toIota(level)
            else -> GarbageIota()
        }
    }

    @JvmStatic
    fun HashMap<*,*>.toIota(level: ServerLevel): Iota {
        if (this.keys.all { it is DoubleIota }) {
            return ListIota((this.filterKeys { it is Number } as Map<Number, Any>).toSortedMap(compareBy { it.toLong() }).mapValues { it.value.toIota(level) }.values.toList())
        } else if (this.keys.all { it is String }) {
            // NBT, defaulting to `IotaType`
        } else {
            // Map
        }
    }
}