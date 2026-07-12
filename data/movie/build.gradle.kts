import java.util.Properties

plugins {
    id("amro.android.library")
    id("amro.hilt")
    alias(libs.plugins.kotlin.serialization)
}

val tmdbProperties = Properties().apply {
    val file = rootProject.file("tmdb.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

android {
    namespace = "com.amro.data.movie"
    buildFeatures.buildConfig = true
    defaultConfig {
        buildConfigField(
            "String",
            "TMDB_ACCESS_TOKEN",
            "\"${tmdbProperties.getProperty("TMDB_ACCESS_TOKEN", "")}\"",
        )
    }
}
dependencies {
    implementation(projects.domain.movie)
    implementation(projects.application.movie)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.dispatchers)
    implementation(projects.core.network)
    implementation(projects.core.logging)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.paging)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    ksp(libs.androidx.room.compiler)

    testImplementation(projects.core.testing)
    testImplementation(libs.mockwebserver)
}
