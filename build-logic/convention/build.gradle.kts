plugins {
    `kotlin-dsl`
}

group = "com.amro.buildlogic"

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.compiler.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.hilt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "amro.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "amro.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("kotlinLibrary") {
            id = "amro.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }

        register("compose") {
            id = "amro.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        register("feature") {
            id = "amro.feature"
            implementationClass = "FeatureConventionPlugin"
        }

        register("hilt") {
            id = "amro.hilt"
            implementationClass = "HiltConventionPlugin"
        }
    }
}