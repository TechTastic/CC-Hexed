package io.github.techtastic.cc_hexed.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import io.github.techtastic.cc_hexed.CCHexed
import io.github.techtastic.cc_hexed.networking.CCHexedNetworking
import io.github.techtastic.cc_hexed.networking.handler.applyOnClient
import io.github.techtastic.cc_hexed.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface CCHexedMessage

sealed interface CCHexedMessageC2S : CCHexedMessage {
    fun sendToServer() {
        CCHexedNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface CCHexedMessageS2C : CCHexedMessage {
    fun sendToPlayer(player: ServerPlayer) {
        CCHexedNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        CCHexedNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface CCHexedMessageCompanion<T : CCHexedMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                CCHexed.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is CCHexedMessageC2S -> msg.applyOnServer(ctx)
                    else -> CCHexed.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                CCHexed.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is CCHexedMessageS2C -> msg.applyOnClient(ctx)
                    else -> CCHexed.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
