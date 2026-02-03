package io.github.techtastic.cc_hexed

import io.github.techtastic.cc_hexed.config.CCHexedClientConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object CCHexedClient {
    fun init() {
        CCHexedClientConfig.init()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(CCHexedClientConfig.GlobalConfig::class.java, parent).get()
    }
}
