package io.github.techtastic.cc_hexed.items

import at.petrak.hexcasting.api.item.PigmentItem
import at.petrak.hexcasting.api.pigment.ColorProvider
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import java.util.UUID

class VirtualPigment(properties: Properties) : Item(properties), PigmentItem {
    override fun provideColor(stack: ItemStack, owner: UUID): ColorProvider =
        VirtualColorProvider(stack.orCreateTag.getInt("rgba"))

    class VirtualColorProvider(val rgba: Int) : ColorProvider() {
        override fun getRawColor(time: Float, position: Vec3): Int = this.rgba
    }
}