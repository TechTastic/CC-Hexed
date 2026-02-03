package io.github.techtastic.cc_hexed.forge

import io.github.techtastic.cc_hexed.CCHexed
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent

object ForgeCCHexedServer {
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLDedicatedServerSetupEvent) {
        CCHexed.initServer()
    }
}
