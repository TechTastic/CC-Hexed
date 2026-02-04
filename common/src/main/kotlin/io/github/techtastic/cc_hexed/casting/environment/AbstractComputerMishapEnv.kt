package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3

abstract class AbstractComputerMishapEnv(world: ServerLevel, caster: ServerPlayer?) : MishapEnvironment(world, caster) {
    override fun yeetHeldItemsTowards(targetPos: Vec3?) {}

    override fun dropHeldItems() {}

    override fun drown() {}

    override fun damage(healthProportion: Float) {}

    override fun removeXp(amount: Int) {}

    override fun blind(ticks: Int) {}
}
