plugins {
    id("dev.isxander.modstitch.base") version "0.8.4"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String;

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitch.loom {
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric-api")}")

        if (sc.current.parsed eq "1.20.1") {
            modstitchModImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:${property("deps.animation")}")
        }

        modstitchCompileOnlyApi("mezz.jei:jei-${property("deps.minecraft")}-fabric-api:${property("deps.jei")}")
        modstitchRuntimeOnly("mezz.jei:jei-${property("deps.minecraft")}-fabric:${property("deps.jei")}")

        modstitchImplementation("org.reflections:reflections:0.10.2")
        modstitchImplementation("org.javassist:javassist:3.29.2-GA")
    }

    modstitch.moddevgradle {

    }

    modstitchModImplementation("me.fzzyhmstrs:fzzy_config:${property("deps.fuzzy")}")
}

modstitch {
    minecraftVersion = minecraft

    // If parchment doesnt exist for a version yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "swm"
        modName = "Syconn's Star Wars Mod"
        modVersion = "1.0.0"
        modGroup = "mod.syconn"
        modAuthor = "Syconn"

        fun <K: Any, V: Any> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://github.com/modunion/modstitch/issues")
            put("pack_format", when (property("deps.minecraft")) {
                "1.20.1" -> 14
                "1.21.1" -> 14
                "26.1.2" -> 14
                else -> throw IllegalArgumentException("Please store the resource pack version for ${property("deps.minecraft")} in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
            }.toString())
        }
    }

    loom {
        fabricLoaderVersion = "0.19.2"

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.named("client") {
                ideConfigGenerated(true)
            }

//            runs {
//                create("datagen") {
//                    client()
//
//                    name("Fabric Data Generation")
//                    vmArg("-Dfabric-api.datagen")
//                    vmArg("-Dfabric-api.datagen.modid=swm")
//                    vmArg("-Dfabric-api.datagen.output-dir=${project.file("src/generated/resources").absolutePath}")
//                    vmArg("-Dfabric-api.datagen.strict-validation")
//                }
//            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        if (sc.current.project.endsWith("neoforge")) {
            if (sc.current.parsed eq "1.21.1") { neoFormVersion = "1.21.1-20240808.144430" }
            else if (sc.current.parsed eq "26.1.2") { neoFormVersion = "26.1.2-1" }
            neoForgeVersion = "${property("deps.neoforge")}"
        } else {
            forgeVersion = "${property("deps.forge")}"
        }

        defaultRuns()
    }

    mixin {
        addMixinsToModManifest = true
        configs.register("swm")

        // Most of the time you wont ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.split("-")[1]
stonecutter {
    constants {
        put("fabric", modstitch.isLoom)
        put("neoforge", modstitch.isModDevGradleRegular)
    }

    replacements {
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("import net.minecraft.Util;", "import net.minecraft.util.Util;")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("location", "identifier")
        }
    }
}

java {
    withSourcesJar()

    val javaCompact = when {
        stonecutter.eval(stonecutter.current.version, "<=1.20.4") -> JavaVersion.VERSION_17
        stonecutter.eval(stonecutter.current.version, "<=1.21.4") -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_25
    }

    sourceCompatibility = javaCompact
    targetCompatibility = javaCompact
}