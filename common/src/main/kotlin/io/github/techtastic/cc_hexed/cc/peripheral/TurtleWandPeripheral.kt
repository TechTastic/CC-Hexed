package io.github.techtastic.cc_hexed.cc.peripheral

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.server.level.ServerLevel

class TurtleWandPeripheral(val turtleAccess: ITurtleAccess, val turtleSide: TurtleSide) : AbstractWandPeripheral() {
    override val world: ServerLevel
        get() = turtleAccess.level as ServerLevel

    override fun runPattern(args: IArguments) {
        TODO("Not yet implemented")
    }

    @Suppress("CovariantEquals")
    override fun equals(other: IPeripheral?): Boolean = other is TurtleWandPeripheral &&
            other.turtleSide == this.turtleSide && other.turtleAccess == this.turtleAccess
}