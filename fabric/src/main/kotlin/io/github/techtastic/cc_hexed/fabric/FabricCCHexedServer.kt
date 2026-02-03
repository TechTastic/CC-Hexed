package io.github.techtastic.cc_hexed.fabric

import io.github.techtastic.cc_hexed.CCHexed
import net.fabricmc.api.DedicatedServerModInitializer

object FabricCCHexedServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        CCHexed.initServer()
    }
}
