package io.github.techtastic.cc_hexed.util

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel

object ConversionUtil {
    @JvmStatic
    fun Iota.toLua(): Any? {
        return when(this) {
            is NullIota -> null
            is DoubleIota -> this.double
            else -> this.serialize()
        }
    }

    @JvmStatic
    fun Any?.toIota(level: ServerLevel): Iota {
        if (this == null) return NullIota()
        return when (this) {
            is Double -> DoubleIota(this)
            else -> GarbageIota()
        }
    }
}