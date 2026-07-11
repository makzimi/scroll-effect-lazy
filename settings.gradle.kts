pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    // PREFER_PROJECT (not FAIL_ON_PROJECT_REPOS): the Kotlin/Wasm toolchain
    // registers its Node.js and Binaryen download repositories at the project
    // level. FAIL_ON_PROJECT_REPOS rejects them (breaking the Wasm binary), and
    // PREFER_SETTINGS ignores them (so the downloads can't be resolved).
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "scroll-effect-lazy"
include(":sample-common")
include(":sample-android")
include(":scroll-effects")
