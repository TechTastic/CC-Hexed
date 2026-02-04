package io.github.techtastic.cc_hexed.datagen

import at.petrak.hexcasting.common.lib.HexItems
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import io.github.techtastic.cc_hexed.cc.turtle.WandTurtleUpgrade
import net.minecraft.data.PackOutput
import java.util.function.Consumer

class CCHexedTurtleUpgradeProvider(output: PackOutput) : TurtleUpgradeDataProvider(output) {
    override fun addUpgrades(addUpgrade: Consumer<Upgrade<TurtleUpgradeSerialiser<*>>>) {
        simpleWithCustomItem(WandTurtleUpgrade.ID, WandTurtleUpgrade.SERIALISER, HexItems.STAFF_MINDSPLICE).add(addUpgrade)
    }
}
