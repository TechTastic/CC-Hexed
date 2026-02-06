package io.github.techtastic.cc_hexed.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import io.github.techtastic.cc_hexed.items.VirtualPigment
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object CCHexedItems : CCHexedRegistrar<Item>(
    Registries.ITEM,
    { BuiltInRegistries.ITEM },
) {
    val RGB_PIGMENT = register("rgb_pigment") { VirtualPigment(Item.Properties().stacksTo(-1)) }
}
