package io.github.techtastic.cc_hexed.fabric

import io.github.techtastic.cc_hexed.CCHexed
import net.fabricmc.api.ModInitializer

object FabricCCHexed : ModInitializer {
    override fun onInitialize() {
        CCHexed.init()
    }
}
