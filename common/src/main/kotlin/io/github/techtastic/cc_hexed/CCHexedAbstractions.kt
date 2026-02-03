@file:JvmName("CCHexedAbstractions")

package io.github.techtastic.cc_hexed

import dev.architectury.injectables.annotations.ExpectPlatform
import io.github.techtastic.cc_hexed.registry.CCHexedRegistrar

fun initRegistries(vararg registries: CCHexedRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: CCHexedRegistrar<T>) {
    throw AssertionError()
}
