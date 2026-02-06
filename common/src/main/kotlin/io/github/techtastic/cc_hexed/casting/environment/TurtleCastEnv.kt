package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.common.lib.HexItems
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.computer.core.ServerComputer
import dan200.computercraft.shared.turtle.core.InteractDirection
import dan200.computercraft.shared.turtle.core.TurtleBrain
import dan200.computercraft.shared.turtle.core.TurtleDropCommand
import io.github.techtastic.cc_hexed.casting.environment.PocketComputerCastEnv
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import java.util.function.Predicate
import kotlin.math.pow

class TurtleCastEnv(level: ServerLevel, computerAccess: IComputerAccess, val turtleAccess: ITurtleAccess, val turtleSide: TurtleSide) : AbstractComputerCastEnv(level, computerAccess) {
    override val mishapEnv: AbstractComputerMishapEnv<TurtleCastEnv>
        get() = object : AbstractComputerMishapEnv<TurtleCastEnv>(world, null, this) {
            override fun yeetHeldItemsTowards(targetPos: Vec3?) {
                val slot = env.turtleAccess.selectedSlot
                val item = env.turtleAccess.inventory.getItem(slot)
                env.turtleAccess.inventory.setItem(slot, ItemStack.EMPTY)
                val pos = env.turtleAccess.position.center
                val delta = targetPos!!.subtract(pos).normalize().scale(0.5)
                yeetItem(item,pos,delta)
            }

            override fun dropHeldItems() {
                env.turtleAccess.executeCommand(
                    TurtleDropCommand(InteractDirection.FORWARD, 64)
                )
            }
        }

    override val serverComputer: ServerComputer
        get() = (this.turtleAccess as TurtleBrain).owner.serverComputer!!

    override val inventory: Container
        get() = this.turtleAccess.inventory

    override var color: Int
        get() = this.turtleAccess.colour
        set(value) {
            this.turtleAccess.colour = value
        }

    constructor(old: TurtleCastEnv, newWorld: ServerLevel) : this(newWorld, old.computerAccess, old.turtleAccess, old.turtleSide)

    override fun mishapSprayPos(): Vec3 = this.turtleAccess.position.center

    override fun getCastingHand(): InteractionHand =
        this.getCastingEntity()?.let { caster ->
            if (caster.getItemInHand(InteractionHand.MAIN_HAND).item == HexItems.STAFF_MINDSPLICE)
                InteractionHand.MAIN_HAND
            else
                InteractionHand.OFF_HAND
        } ?: InteractionHand.MAIN_HAND

    override fun isVecInRangeEnvironment(vec: Vec3): Boolean {
        return vec.distanceToSqr(this.turtleAccess.position.center) <= PlayerBasedCastEnv.DEFAULT_AMBIT_RADIUS.pow(2.0) // TODO: Multiplier
    }

    override fun hasEditPermissionsAtEnvironment(pos: BlockPos?): Boolean {
        return this.turtleAccess.owningPlayer?.let { profile ->
            (this.world.getPlayerByUUID(profile.id) as? ServerPlayer)?.let { player ->
                this.world.mayInteract(player, pos)
            } ?: false
        } ?: false
    }

    override fun getPrimaryStacks(): List<HeldItemInfo> {
        val slot = this.turtleAccess.selectedSlot
        val leftItem = this.turtleAccess.inventory.getItem(0)
        val rightItem = this.turtleAccess.inventory.getItem(1)
        return mutableListOf(
            HeldItemInfo(this.turtleAccess.inventory.getItem(slot), InteractionHand.OFF_HAND),
            HeldItemInfo(leftItem, if (leftItem.isEmpty || leftItem.item != HexItems.STAFF_MINDSPLICE) InteractionHand.OFF_HAND else InteractionHand.MAIN_HAND),
            HeldItemInfo(rightItem, if (rightItem.isEmpty || rightItem.item != HexItems.STAFF_MINDSPLICE) InteractionHand.OFF_HAND else InteractionHand.MAIN_HAND)
        )
    }

    override fun getHeldItemToOperateOn(stackOk: Predicate<ItemStack>): HeldItemInfo? {
        val inv = this.turtleAccess.inventory
        val slot = this.turtleAccess.selectedSlot
        val item = inv.getItem(slot)
        return if (item == ItemStack.EMPTY || !stackOk.test(item)) {
            null
        } else {
            HeldItemInfo(item,InteractionHand.MAIN_HAND)
        }
    }
}