package io.github.techtastic.cc_hexed.networking

import dev.architectury.networking.NetworkChannel
import io.github.techtastic.cc_hexed.CCHexed
import io.github.techtastic.cc_hexed.networking.msg.CCHexedMessageCompanion

object CCHexedNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(CCHexed.id("networking_channel"))

    fun init() {
        for (subclass in CCHexedMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
