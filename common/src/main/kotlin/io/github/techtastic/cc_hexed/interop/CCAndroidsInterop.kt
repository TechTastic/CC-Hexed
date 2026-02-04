package io.github.techtastic.cc_hexed.interop

import at.petrak.hexcasting.common.lib.HexItems
import com.thunderbear06.computer.peripherals.DummyPocket
import com.thunderbear06.entity.android.BaseAndroidEntity
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.world.Container
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity

object CCAndroidsInterop {
    fun getServerComputer(pocketAccess: IPocketAccess): ServerComputer? {
        if (pocketAccess !is DummyPocket) return null
        return (pocketAccess.entity as? BaseAndroidEntity)?.let { android -> return android.computer.serverComputer }
    }

    fun getCastingHand(pocketAccess: IPocketAccess): InteractionHand? =
        (pocketAccess.entity as? BaseAndroidEntity)?.let { android ->
            if ((android as LivingEntity).getItemInHand(InteractionHand.MAIN_HAND).item == HexItems.STAFF_MINDSPLICE)
                InteractionHand.MAIN_HAND
            else
                InteractionHand.OFF_HAND
        }

    fun getInventory(pocketAccess: IPocketAccess): Container? =
        (pocketAccess.entity as? BaseAndroidEntity)?.inventory
}