package io.github.techtastic.cc_hexed.cc.turtle

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.AbstractPocketUpgrade
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.turtle.TurtleUpgradeType
import io.github.techtastic.cc_hexed.CCHexed
import io.github.techtastic.cc_hexed.casting.environment.TurtleCastEnv
import io.github.techtastic.cc_hexed.cc.peripheral.PocketWandPeripheral
import io.github.techtastic.cc_hexed.cc.peripheral.TurtleWandPeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class WandTurtleUpgrade(val stack: ItemStack): AbstractTurtleUpgrade(ID, TurtleUpgradeType.BOTH, "Magical", stack) {
    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        val peripheral = turtle.getPeripheral(side) ?: return
        if (peripheral is PocketWandPeripheral) {
            if (!peripheral.init) return
            peripheral.vm.image = peripheral.vm.image.copy(opsConsumed = 0)
        }
    }

    override fun createPeripheral(turtle: ITurtleAccess, side: TurtleSide): IPeripheral =
        TurtleWandPeripheral(turtle, side)

    companion object {
        val ID = ResourceLocation(CCHexed.MODID, "wand")
        val SERIALISER = TurtleUpgradeSerialiser.simpleWithCustomItem { location, stack -> WandTurtleUpgrade(stack) }
    }
}