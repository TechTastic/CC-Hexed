@file:JvmName("CCHexedAbstractionsImpl")

package io.github.techtastic.cc_hexed.forge

import io.github.techtastic.cc_hexed.registry.CCHexedRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: CCHexedRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
