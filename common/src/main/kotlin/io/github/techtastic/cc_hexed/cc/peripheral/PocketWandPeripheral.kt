package io.github.techtastic.cc_hexed.cc.peripheral

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.IPocketAccess
import net.minecraft.server.level.ServerLevel

class PocketWandPeripheral(val pocketAccess: IPocketAccess) : AbstractWandPeripheral() {
    override val world: ServerLevel
        get() = pocketAccess.level

    override fun runPattern(args: IArguments) {
        TODO("Not yet implemented")
    }

    @Suppress("CovariantEquals")
    override fun equals(other: IPeripheral?): Boolean = other is PocketWandPeripheral && other.pocketAccess == this.pocketAccess
}