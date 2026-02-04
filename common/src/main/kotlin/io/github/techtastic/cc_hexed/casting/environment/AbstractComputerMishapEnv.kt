package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

abstract class AbstractComputerMishapEnv<T: AbstractComputerCastEnv>(world: ServerLevel, caster: ServerPlayer?, val env: T) : MishapEnvironment(world, caster) {
    override fun yeetHeldItemsTowards(targetPos: Vec3?) {
        val pos = env.castingEntity!!.position()
        val delta = targetPos!!.subtract(pos).normalize().scale(0.5)

        for (hand in InteractionHand.entries) {
            val stack = env.caster!!.getItemInHand(hand)
            env.caster!!.setItemInHand(hand, ItemStack.EMPTY)
            this.yeetItem(stack, pos, delta)
        }
    }

    override fun dropHeldItems() {
        this.yeetHeldItemsTowards(env.castingEntity!!.position().add(env.castingEntity!!.lookAngle))
    }

    override fun drown() {}

    override fun damage(healthProportion: Float) {}

    override fun removeXp(amount: Int) {}

    override fun blind(ticks: Int) {}
}
