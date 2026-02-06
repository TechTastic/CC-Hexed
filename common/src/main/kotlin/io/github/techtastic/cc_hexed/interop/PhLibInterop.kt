package io.github.techtastic.cc_hexed.interop

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import io.github.techtastic.cc_hexed.util.ConversionUtil.toIota
import io.github.techtastic.cc_hexed.util.ConversionUtil.toLua
import net.minecraft.server.level.ServerLevel
import org.eu.net.pool.phlib.MapIota
import scala.jdk.CollectionConverters

object PhLibInterop {
    fun toIota(obj: Any, world: ServerLevel): Iota? =
        (obj as? HashMap<*,*>)?.let { map ->
            return@let MapIota.fromJavaMap(map.mapKeys { (key, _) ->
                key.toIota(world)
            }.mapValues { (_, value) ->
                value.toIota(world)
            }, world)
        }

    fun toLua(iota: Iota, world: ServerLevel): Any? =
        (iota as? MapIota)?.map()?.let { map ->
            CollectionConverters.MapHasAsJava(map).asJava()
            .mapKeys { (key, _) ->
                IotaType.deserialize(key, world).toLua(world)
            }.mapValues { (_, value) ->
                IotaType.deserialize(value, world).toLua(world)
            }
        }
}