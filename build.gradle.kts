plugins {
    id("cc_hexed.java")
    id("cc_hexed.mod-publish")
}


architectury {
    // this looks up the value from gradle/libs.versions.toml
    minecraft = libs.versions.minecraft.get()
}

allprojects {
    repositories {
        maven {
            url = uri("https://maven.squiddev.cc")
            content {
                includeGroup("cc.tweaked")
            }
        }

        maven {
            name = "Modrinth"
            url = uri("https://api.modrinth.com/maven")
            content {
                includeGroup("maven.modrinth")
            }
        }

        maven {
            name = "pool maven"
            url = uri("https://pool.net.eu.org/")
            content {
                includeGroup("org.eu.net.pool")
                includeGroup("poollovernathan.fabric")
            }
        }
    }
}

tasks {
    register("runAllDatagen") {
        dependsOn(":fabric:runDatagen")
        dependsOn(":forge:runCommonDatagen")
        dependsOn(":forge:runForgeDatagen")
    }
}

publishMods {
    displayName = "v${project.version}"

    github {
        repository = System.getenv("GITHUB_REPOSITORY") ?: ""
        commitish = System.getenv("GITHUB_SHA") ?: ""

        // https://modmuss50.github.io/mod-publish-plugin/platforms/github/#parent-releases
        allowEmptyFiles = true
    }
}
