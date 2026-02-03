package io.github.techtastic.cc_hexed.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.techtastic.cc_hexed.CCHexedClient

object FabricCCHexedModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(CCHexedClient::getConfigScreen)
}
