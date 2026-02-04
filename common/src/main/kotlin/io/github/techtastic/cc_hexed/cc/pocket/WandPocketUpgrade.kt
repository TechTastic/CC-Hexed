package io.github.techtastic.cc_hexed.cc.pocket

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.AbstractPocketUpgrade
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import io.github.techtastic.cc_hexed.CCHexed.MODID
import io.github.techtastic.cc_hexed.cc.peripheral.PocketWandPeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class WandPocketUpgrade(val stack: ItemStack): AbstractPocketUpgrade(ID, "Magical", stack) {
    override fun update(access: IPocketAccess, peripheral: IPeripheral?) {
        if (peripheral is PocketWandPeripheral) {
            if (!peripheral.init) return
            peripheral.vm.image = peripheral.vm.image.copy(opsConsumed = 0)
        }
    }

    override fun createPeripheral(access: IPocketAccess): IPeripheral = PocketWandPeripheral(access)

    companion object {
        val ID = ResourceLocation(MODID, "wand")
        val SERIALISER = PocketUpgradeSerialiser.simpleWithCustomItem { location, stack -> WandPocketUpgrade(stack) }
    }
}