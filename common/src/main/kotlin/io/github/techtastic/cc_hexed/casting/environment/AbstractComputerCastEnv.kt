package io.github.techtastic.cc_hexed.casting.environment

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.compareMediaItem
import at.petrak.hexcasting.api.utils.otherHand
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

abstract class AbstractComputerCastEnv(val level: ServerLevel, val computerAccess: IComputerAccess) : CastingEnvironment(level) {
    abstract val mishapEnv: AbstractComputerMishapEnv<*>
    abstract val serverComputer: ServerComputer
    abstract val inventory: Container?

    override fun getCastingEntity(): LivingEntity? = null

    override fun getMishapEnvironment(): MishapEnvironment = mishapEnv

    override fun printMessage(message: Component) {
        this.computerAccess.queueEvent("reveal", this.computerAccess.attachmentName, message.string)
    }

    override fun extractMediaEnvironment(cost: Long, simulate: Boolean): Long {
        val inventory = this.inventory ?: return cost
        var cost = cost // TODO: Multiplier
        val adMediaHolders: ArrayList<ADMediaHolder> = ArrayList()
        for (i in 0..inventory.containerSize) {
            val item = inventory.getItem(i)
            val media = HexAPI.instance().findMediaHolder(item)
            if (media?.canProvide() == true) {
                adMediaHolders.add(media)
            }
        }
        adMediaHolders.sortWith(::compareMediaItem)
        adMediaHolders.reverse()
        for (source in adMediaHolders) {
            val found = source.withdrawMedia(cost, simulate)
            cost -= found
            if (cost <= 0) {
                break
            }
        }
        return cost
    }

    override fun getUsableStacks(mode: StackDiscoveryMode?): List<ItemStack> {
        val out = ArrayList<ItemStack>()
        //TODO: check back how base hexcasting handles these modes after I deprecated caster
        val castingPlayer = this.castingEntity as? ServerPlayer
        if (castingPlayer != null) {
            when (mode) {
                StackDiscoveryMode.QUERY -> {
                    val offhand = castingPlayer.getItemInHand(otherHand(this.castingHand))
                    if (!offhand.isEmpty) {
                        out.add(offhand)
                    }


                    // If we're casting from the main hand, try to pick from the slot one to the right of the selected slot
                    // Otherwise, scan the hotbar left to right
                    val anchorSlot = if (this.castingHand == InteractionHand.MAIN_HAND
                    ) (castingPlayer.inventory.selected + 1) % 9
                    else 0


                    for (delta in 0..8) {
                        val slot = (anchorSlot + delta) % 9
                        out.add(castingPlayer.inventory.getItem(slot))
                    }
                }
                StackDiscoveryMode.EXTRACTION -> {
                    // https://wiki.vg/Inventory is WRONG
                    // slots 0-8 are the hotbar
                    // for what purpose I cannot imagine
                    // http://redditpublic.com/images/b/b2/Items_slot_number.png looks right
                    // and offhand is 150 Inventory.java:464

                    // First, the inventory backwards
                    // We use inv.items here to get the main inventory, but not offhand or armor
                    val inv = castingPlayer.inventory
                    for (i in inv.items.indices.reversed()) {
                        if (i != inv.selected) {
                            out.add(inv.items[i])
                        }
                    }


                    // then the offhand, then the selected hand
                    out.addAll(inv.offhand)
                    out.add(inv.getSelected())
                }
                else -> {}
            }
        } else if (this.inventory != null) {
            for (i in 0..this.inventory!!.containerSize) {
                val item = this.inventory!!.getItem(i)
                out.add(item)
            }
        }
        return out
    }

    override fun replaceItem(stackOk: Predicate<ItemStack>, replaceWith: ItemStack, hand: InteractionHand?): Boolean = false

    override fun getPigment(): FrozenPigment? {
        // TODO: RGB Pigment
        return FrozenPigment.DEFAULT.get()
    }

    override fun setPigment(pigment: FrozenPigment?): FrozenPigment? {
        // TODO: RBG PIGMENT
        return pigment
    }

    override fun produceParticles(particles: ParticleSpray, colorizer: FrozenPigment) =
        particles.sprayParticles(this.world, pigment ?: colorizer)

    override fun isEnlightened(): Boolean =
        this.serverComputer.family != ComputerFamily.NORMAL

    override fun isCreativeMode(): Boolean =
        this.serverComputer.family == ComputerFamily.COMMAND || (this.castingEntity as? Player)?.isCreative == true

    // TODO: Turn this into a PostExecution extension
    override fun postExecution(result: CastResult?) {
        super.postExecution(result)

        for (sideEffect in result!!.sideEffects) {
            if (sideEffect is OperatorSideEffect.DoMishap) {
                val msg = sideEffect.mishap.errorMessageWithName(this, sideEffect.errorCtx)
                if (msg != null) {
                    this.computerAccess.queueEvent(
                        "mishap",
                        this.computerAccess.attachmentName,
                        sideEffect.mishap.javaClass.name,
                        msg,
                        sideEffect.errorCtx.pattern.toString()
                    )
                }
            }
        }
    }
}