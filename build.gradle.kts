plugins {
    // id("dev.isxander.modstitch.base") version "0.5.12"
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

        modstitchImplementation("org.reflections:reflections:0.10.2")
        modstitchImplementation("org.javassist:javassist:3.29.2-GA")
    }

    // Anything else in the dependencies block will be used for all platforms.
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

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure its up to date.
        fabricLoaderVersion = "0.19.2"

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.named("client") {
                ideConfigGenerated(true)
            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {

        if (sc.current.project.endsWith("neoforge")) {

            if (sc.current.parsed eq "1.21.1") { neoFormVersion = "1.21.1-20240808.144430" }
            else if (sc.current.parsed eq "26.1.2") { neoFormVersion = "26.1.2-1" }

            neoForgeVersion = "${property("deps.neoforge")}"
        } else
        {
            forgeVersion = "${property("deps.forge")}"
        }

//        enable {
//            prop("deps.neoform") { neoFormVersion = it }
//            prop("deps.neoforge") { neoForgeVersion = it }
//            prop("deps.mcp") { mcpVersion = it }
//        }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()

        // This block configures the `neoforge` extension that MDG exposes by default,
        // you can configure MDG like normal from here
//        configureNeoforge {
//            runs.all {
//                disableIdeRun()
//            }
//        }
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

    val javaCompat = when {
        stonecutter.eval(stonecutter.current.version, "<=1.20.4") -> JavaVersion.VERSION_17
        stonecutter.eval(stonecutter.current.version, "<=1.21.4") -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_25
    }
//    val javaCompat = if (stonecutter.eval(stonecutter.current.version, "<=1.21.1")) {
//        JavaVersion.VERSION_21
//    } else {
//        JavaVersion.VERSION_25
//    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}