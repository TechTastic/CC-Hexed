package io.github.techtastic.cc_hexed.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import io.github.techtastic.cc_hexed.config.CCHexedServerConfig
import io.github.techtastic.cc_hexed.networking.msg.*

fun CCHexedMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            CCHexedServerConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
