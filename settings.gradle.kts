pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AMRO"

include(
    ":app",
    ":core:common",
    ":core:database",
    ":core:datastore",
    ":core:designsystem",
    ":core:dispatchers",
    ":core:logging",
    ":core:network",
    ":core:testing",
    ":domain:movie",
    ":application:movie",
    ":data:movie",
    ":feature:trending",
)
