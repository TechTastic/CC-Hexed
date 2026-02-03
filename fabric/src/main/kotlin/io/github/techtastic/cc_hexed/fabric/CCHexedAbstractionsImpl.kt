@file:JvmName("CCHexedAbstractionsImpl")

package io.github.techtastic.cc_hexed.fabric

import io.github.techtastic.cc_hexed.registry.CCHexedRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: CCHexedRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
