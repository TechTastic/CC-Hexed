package io.github.techtastic.cc_hexed.fabric

import io.github.techtastic.cc_hexed.CCHexedClient
import net.fabricmc.api.ClientModInitializer

object FabricCCHexedClient : ClientModInitializer {
    override fun onInitializeClient() {
        CCHexedClient.init()
    }
}
