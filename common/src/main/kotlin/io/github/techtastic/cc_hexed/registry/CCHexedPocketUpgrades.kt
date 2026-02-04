package io.github.techtastic.cc_hexed.registry

import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import io.github.techtastic.cc_hexed.cc.pocket.WandPocketUpgrade
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object CCHexedPocketUpgrades : CCHexedRegistrar<PocketUpgradeSerialiser<*>>(
    PocketUpgradeSerialiser.registryId(),
    { BuiltInRegistries.REGISTRY.get(PocketUpgradeSerialiser.registryId().location()) as Registry<PocketUpgradeSerialiser<*>> },
) {
    val WAND = register<PocketUpgradeSerialiser<*>>(WandPocketUpgrade.ID, WandPocketUpgrade::SERIALISER)
}
