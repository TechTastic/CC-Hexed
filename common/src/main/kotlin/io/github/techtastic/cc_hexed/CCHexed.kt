package io.github.techtastic.cc_hexed

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.techtastic.cc_hexed.config.CCHexedServerConfig
import io.github.techtastic.cc_hexed.networking.CCHexedNetworking
import io.github.techtastic.cc_hexed.registry.CCHexedActions

object CCHexed {
    const val MODID = "cc_hexed"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        CCHexedServerConfig.init()
        initRegistries(
            CCHexedActions,
        )
        CCHexedNetworking.init()
    }

    fun initServer() {
        CCHexedServerConfig.initServer()
    }
}
