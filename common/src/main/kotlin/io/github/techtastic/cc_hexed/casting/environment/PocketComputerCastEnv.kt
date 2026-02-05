package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.shared.computer.core.ServerComputer
import dev.architectury.platform.Platform
import io.github.techtastic.cc_hexed.interop.CCAndroidsInterop
import io.github.techtastic.cc_hexed.interop.PlethoraInterop
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.GameType
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

class PocketComputerCastEnv(level: ServerLevel, computerAccess: IComputerAccess, val pocketAccess: IPocketAccess) : AbstractComputerCastEnv(level, computerAccess) {
    override val mishapEnv: AbstractComputerMishapEnv<PocketComputerCastEnv>
        get() = object : AbstractComputerMishapEnv<PocketComputerCastEnv>(world, this.castingEntity as? ServerPlayer, this) {}

    override val serverComputer: ServerComputer
        get() = run {
            if (Platform.isModLoaded("plethora"))
                PlethoraInterop.getServerComputer(this.pocketAccess)?.let { return it }
            if (Platform.isModLoaded("cc-androids"))
                CCAndroidsInterop.getServerComputer(this.pocketAccess)?.let { return it }
            return this.pocketAccess as ServerComputer
        }

    override val inventory: Container
        get() = run {
            if (Platform.isModLoaded("cc-androids"))
                CCAndroidsInterop.getInventory(this.pocketAccess)?.let { return it }
            (this.castingEntity as? ServerPlayer)?.inventory as Container
        }

    constructor(old: PocketComputerCastEnv, newWorld: ServerLevel) : this(newWorld, old.computerAccess, old.pocketAccess)

    override fun getCastingEntity(): LivingEntity? = this.pocketAccess.entity as? LivingEntity

    override fun mishapSprayPos(): Vec3 = this.pocketAccess.position

    override fun getCastingHand(): InteractionHand {
        if (Platform.isModLoaded("cc-androids"))
            CCAndroidsInterop.getCastingHand(this.pocketAccess)?.let { return it }

        return InteractionHand.MAIN_HAND
    }

    override fun isVecInRangeEnvironment(vec: Vec3): Boolean {
        (this.castingEntity as? ServerPlayer)?.let { player ->
            HexAPI.instance().getSentinel(player)?.let { sentinel ->
                return sentinel.extendsRange() && player.level().dimension() === sentinel.dimension() &&
                        vec.distanceToSqr(this.pocketAccess.position) <= PlayerBasedCastEnv.DEFAULT_SENTINEL_RADIUS.pow(2.0) // TODO: Multiplier
            }
        }
        return vec.distanceToSqr(this.pocketAccess.position) <= PlayerBasedCastEnv.DEFAULT_AMBIT_RADIUS.pow(2.0) // TODO: Multiplier
    }

    override fun hasEditPermissionsAtEnvironment(pos: BlockPos?): Boolean {
        return (this.castingEntity as? ServerPlayer)?.let { player ->
            player.gameMode.gameModeForPlayer != GameType.ADVENTURE && this.world.mayInteract(player, pos)
        } ?: true
    }

    override fun getPrimaryStacks(): List<HeldItemInfo> {
        val ent = this.castingEntity ?: return mutableListOf()
        return mutableListOf(
            HeldItemInfo(ent.getItemInHand(this.otherHand), this.otherHand),
            HeldItemInfo(ent.getItemInHand(this.castingHand), this.castingHand)
        )
    }
}