package io.github.techtastic.cc_hexed.interop

import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.shared.computer.core.ServerComputer
import io.sc3.plethora.gameplay.neural.NeuralPocketAccess

object PlethoraInterop {
    fun getServerComputer(pocketAccess: IPocketAccess): ServerComputer? {
        if (pocketAccess !is NeuralPocketAccess) return null

        try {
            val clazz = NeuralPocketAccess::class.java
            val field = clazz.getDeclaredField("neural")
            field.isAccessible = true
            return field.get(pocketAccess) as? ServerComputer
        } catch (_: Exception) {
            return null
        }
    }
}