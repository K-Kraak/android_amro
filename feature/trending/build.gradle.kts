plugins {
    id("amro.feature")
    id("amro.hilt")
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.amro.feature.trending"
}
dependencies {
    implementation(projects.application.movie)
    implementation(projects.domain.movie)
    implementation(projects.core.designsystem)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.compose.material.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(projects.core.testing)
}
