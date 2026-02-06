package io.github.techtastic.cc_hexed

import dan200.computercraft.api.client.ComputerCraftAPIClient
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import io.github.techtastic.cc_hexed.cc.turtle.WandTurtleUpgrade
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.techtastic.cc_hexed.config.CCHexedServerConfig
import io.github.techtastic.cc_hexed.registry.CCHexedActions
import io.github.techtastic.cc_hexed.registry.CCHexedItems
import io.github.techtastic.cc_hexed.registry.CCHexedPocketUpgrades
import io.github.techtastic.cc_hexed.registry.CCHexedTurtleUpgrades

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
            CCHexedPocketUpgrades,
            CCHexedTurtleUpgrades,
            CCHexedItems
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(WandTurtleUpgrade.SERIALISER, TurtleUpgradeModeller.flatItem())
    }

    fun initServer() {
        CCHexedServerConfig.initServer()
    }
}
