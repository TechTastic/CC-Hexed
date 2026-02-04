package io.github.techtastic.cc_hexed.registry

import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import io.github.techtastic.cc_hexed.cc.turtle.WandTurtleUpgrade
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object CCHexedTurtleUpgrades : CCHexedRegistrar<TurtleUpgradeSerialiser<*>>(
    TurtleUpgradeSerialiser.registryId(),
    { BuiltInRegistries.REGISTRY.get(TurtleUpgradeSerialiser.registryId().location()) as Registry<TurtleUpgradeSerialiser<*>> },
) {
    val WAND = register(WandTurtleUpgrade.ID, WandTurtleUpgrade::SERIALISER)
}
