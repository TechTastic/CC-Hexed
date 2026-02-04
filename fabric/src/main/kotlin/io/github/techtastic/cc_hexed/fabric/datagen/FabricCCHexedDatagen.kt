package io.github.techtastic.cc_hexed.fabric.datagen

import io.github.techtastic.cc_hexed.datagen.CCHexedActionTags
import io.github.techtastic.cc_hexed.datagen.CCHexedPocketUpgradeProvider
import io.github.techtastic.cc_hexed.datagen.CCHexedTurtleUpgradeProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object FabricCCHexedDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        pack.addProvider(::CCHexedActionTags)
        pack.addProvider(::CCHexedPocketUpgradeProvider)
        pack.addProvider(::CCHexedTurtleUpgradeProvider)
    }
}
