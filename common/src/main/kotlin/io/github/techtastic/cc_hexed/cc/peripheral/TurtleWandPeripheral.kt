package io.github.techtastic.cc_hexed.cc.peripheral

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import io.github.techtastic.cc_hexed.casting.environment.TurtleCastEnv
import io.github.techtastic.cc_hexed.util.ConversionUtil.toIota
import net.minecraft.server.level.ServerLevel

class TurtleWandPeripheral(val turtleAccess: ITurtleAccess, val turtleSide: TurtleSide) : AbstractWandPeripheral() {
    override val world: ServerLevel
        get() = turtleAccess.level as ServerLevel

    override fun attach(computer: IComputerAccess) {
        vm = CastingVM(
            CastingImage(),
            TurtleCastEnv(world, computer, turtleAccess, turtleSide)
        )

        super.attach(computer)
    }

    @LuaFunction(mainThread = true)
    override fun runPattern(args: IArguments) {
        val iota = when (args.count()) {
            0 -> PatternIota(HexActions.EVAL.prototype)
            1 -> {
                val obj = args.getTable(0)
                obj.toIota(world)
            }
            2 -> PatternIota(HexPattern.fromAngles(args.getString(1), HexDir.fromString(args.getString(0))))
            else -> GarbageIota()
        }
        if (vm.env.world != world)
            vm = CastingVM(vm.image, TurtleCastEnv(vm.env as TurtleCastEnv, world))
        vm.queueExecuteAndWrapIota(iota, world)
    }

    @Suppress("CovariantEquals")
    override fun equals(other: IPeripheral?): Boolean = other is TurtleWandPeripheral &&
            other.turtleSide == this.turtleSide && other.turtleAccess == this.turtleAccess
}