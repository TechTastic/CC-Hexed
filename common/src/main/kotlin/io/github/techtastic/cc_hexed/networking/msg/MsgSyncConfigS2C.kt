package io.github.techtastic.cc_hexed.networking.msg

import io.github.techtastic.cc_hexed.config.CCHexedServerConfig
import net.minecraft.network.FriendlyByteBuf

data class MsgSyncConfigS2C(val serverConfig: CCHexedServerConfig.ServerConfig) : CCHexedMessageS2C {
    companion object : CCHexedMessageCompanion<MsgSyncConfigS2C> {
        override val type = MsgSyncConfigS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = MsgSyncConfigS2C(
            serverConfig = CCHexedServerConfig.ServerConfig().decode(buf),
        )

        override fun MsgSyncConfigS2C.encode(buf: FriendlyByteBuf) {
            serverConfig.encode(buf)
        }
    }
}
