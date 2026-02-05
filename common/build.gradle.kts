plugins {
    id("cc_hexed.minecraft")
}

architectury {
    common("fabric", "forge")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(kotlin("reflect"))

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation(libs.fabric.loader)
    modApi(libs.architectury)

    modApi(libs.hexcasting.common)

    modApi(libs.clothConfig.common)

    modCompileOnly(libs.cc.tweaked.common.api)
    modCompileOnly(libs.cc.tweaked.common)
    modCompileOnly(libs.cc.androids.fabric)
    modCompileOnly(libs.cc.drones.plus)
    modCompileOnly(libs.plethora.peripherals)

    modCompileOnly(libs.moreiotas.common)
    compileOnly(libs.jblas)
    compileOnly(libs.complexhex)

    libs.mixinExtras.common.also {
        implementation(it)
        annotationProcessor(it)
    }
}

tasks.register<Exec>("updateSubmodules") {
    commandLine("git", "submodule", "update", "--recursive", "--remote")
}

tasks.register<Copy>("copyMathLibrary") {
    dependsOn("updateSubmodules")
    from("../libs/advanced_math/datapack/data/computercraft/lua/rom/")
    into("src/main/resources/data/computercraft/lua/rom/")
    include("**/*.lua")
    include("*/*.txt")
}

tasks.named("processResources") {
    dependsOn("copyMathLibrary")
}

tasks.named("sourcesJar") {
    dependsOn("copyMathLibrary")
}
