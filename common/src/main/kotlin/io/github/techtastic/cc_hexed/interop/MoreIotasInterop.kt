package io.github.techtastic.cc_hexed.interop

import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.moreiotas.api.casting.iota.StringIota

object MoreIotasInterop {
    fun toIota(obj: Any): Iota? =
        (obj as? String)?.let { return@let StringIota.make(it) }

    fun toLua(iota: Iota): Any? =
        (iota as? StringIota)?.string
}