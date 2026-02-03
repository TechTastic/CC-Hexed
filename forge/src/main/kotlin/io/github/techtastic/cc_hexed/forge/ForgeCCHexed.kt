package io.github.techtastic.cc_hexed.forge

import dev.architectury.platform.forge.EventBuses
import io.github.techtastic.cc_hexed.CCHexed
import io.github.techtastic.cc_hexed.forge.datagen.ForgeCCHexedDatagen
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(CCHexed.MODID)
class ForgeCCHexed {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(CCHexed.MODID, this)
            addListener(ForgeCCHexedClient::init)
            addListener(ForgeCCHexedDatagen::init)
            addListener(ForgeCCHexedServer::init)
        }
        CCHexed.init()
    }
}
