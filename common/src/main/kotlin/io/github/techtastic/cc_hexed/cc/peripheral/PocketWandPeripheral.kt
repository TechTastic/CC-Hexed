package io.github.techtastic.cc_hexed.cc.peripheral

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.IPocketAccess
import io.github.techtastic.cc_hexed.casting.environment.PocketComputerCastEnv
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

    override fun runPattern(args: IArguments) {
        TODO("Not yet implemented")
    }

    @Suppress("CovariantEquals")
    override fun equals(other: IPeripheral?): Boolean = other is PocketWandPeripheral && other.pocketAccess == this.pocketAccess
}