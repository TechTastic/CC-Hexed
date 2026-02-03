package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.server.level.ServerLevel

abstract class AbstractComputerCastEnv(level: ServerLevel) : CastingEnvironment(level) {
}