package io.github.techtastic.cc_hexed.cc.peripheral

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.IPocketAccess
import io.github.techtastic.cc_hexed.casting.environment.PocketComputerCastEnv
import io.github.techtastic.cc_hexed.casting.environment.TurtleCastEnv
import io.github.techtastic.cc_hexed.util.ConversionUtil.toIota
import net.minecraft.server.level.ServerLevel

class PocketWandPeripheral(val pocketAccess: IPocketAccess) : AbstractWandPeripheral() {
    override val world: ServerLevel
        get() = pocketAccess.level

    override fun attach(computer: IComputerAccess) {
        vm = CastingVM(
            CastingImage(),
            PocketComputerCastEnv(world, computer, pocketAccess)
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
            vm = CastingVM(vm.image, PocketComputerCastEnv(vm.env as PocketComputerCastEnv, world))
        vm.queueExecuteAndWrapIota(iota, world)
    }

    @Suppress("CovariantEquals")
    override fun equals(other: IPeripheral?): Boolean = other is PocketWandPeripheral && other.pocketAccess == this.pocketAccess
}