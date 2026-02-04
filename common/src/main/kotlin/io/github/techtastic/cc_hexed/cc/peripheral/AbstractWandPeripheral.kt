package io.github.techtastic.cc_hexed.cc.peripheral

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.shared.util.NBTUtil
import io.github.techtastic.cc_hexed.util.ConversionUtil.toIota
import io.github.techtastic.cc_hexed.util.ConversionUtil.toLua
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel

abstract class AbstractWandPeripheral: IPeripheral {
    lateinit var vm: CastingVM
    var init = false
    abstract val world: ServerLevel

    override fun getType(): String = "wand"

    override fun attach(computer: IComputerAccess) {
        init = true
    }

    override fun detach(computer: IComputerAccess?) {
        init = false
    }

    @LuaFunction
    fun test(args: IArguments): Any? {
        return args.get(0)?.toIota(world)?.toLua()
    }

    @LuaFunction
    fun getStack(): MethodResult {
        return MethodResult.of(vm.image.stack.map { it.toLua() })
    }

    @LuaFunction
    fun clearStack(): Int {
        vm.image = vm.image.copy(stack = vm.image.stack.toMutableList())
        val size = vm.image.stack.size
        (vm.image.stack as MutableList).clear()
        return size
    }

    @Suppress("UNCHECKED_CAST")
    @LuaFunction
    fun setStack(stack: Map<*,*>) {
        vm.image = vm.image.copy(stack = vm.image.stack.toMutableList())
        (vm.image.stack as MutableList).clear()
        (vm.image.stack as MutableList).addAll((stack.filter { it.key is Number } as Map<Number,Any>).toSortedMap(compareBy { it.toLong() }).map {
            it.value.toIota(world)
        })
    }

    @LuaFunction
    fun pushStack(obj: Any?) {
        val stack = vm.image.stack.toMutableList()
        val iota = obj.toIota(world)
        stack.add(iota)
        vm.image = vm.image.copy(stack) // please petrak I am crying and begging. make the stack mutable
    }

    @LuaFunction
    fun popStack(): Any? {
        vm.image = vm.image.copy(stack = vm.image.stack.toMutableList())
        val iota = (vm.image.stack as MutableList).removeLast()
        return iota.toLua()
    }

    @LuaFunction
    fun isEnlightened(): Boolean = vm.env.isEnlightened

    @LuaFunction
    fun getRavenmind(): Any? {
        val nbt = vm.image.userData.getCompound(HexAPI.RAVENMIND_USERDATA)
        val iota = IotaType.deserialize(nbt, world)
        return iota.toLua()
    }

    @LuaFunction
    fun setRavenmind(iota: Any) {
        val newLocal = iota.toIota(world)
        if ((newLocal?.type ?: HexIotaTypes.NULL) == HexIotaTypes.NULL)
            vm.image.userData.remove(HexAPI.RAVENMIND_USERDATA)
        else
            vm.image.userData.put(HexAPI.RAVENMIND_USERDATA, IotaType.serialize(newLocal))
    }

    @LuaFunction(mainThread = true)
    abstract fun runPattern(args: IArguments)
}