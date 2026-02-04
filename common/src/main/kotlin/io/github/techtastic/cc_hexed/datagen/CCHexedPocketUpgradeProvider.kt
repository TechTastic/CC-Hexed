package io.github.techtastic.cc_hexed.datagen

import at.petrak.hexcasting.common.lib.HexItems
import dan200.computercraft.api.pocket.PocketUpgradeDataProvider
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import io.github.techtastic.cc_hexed.cc.pocket.WandPocketUpgrade
import net.minecraft.data.PackOutput
import java.util.function.Consumer

class CCHexedPocketUpgradeProvider(output: PackOutput) : PocketUpgradeDataProvider(output) {
    override fun addUpgrades(addUpgrade: Consumer<Upgrade<PocketUpgradeSerialiser<*>>>) {
        simpleWithCustomItem(WandPocketUpgrade.ID, WandPocketUpgrade.SERIALISER, HexItems.STAFF_MINDSPLICE).add(addUpgrade)
    }
}
