plugins {
    id("amro.feature")
    id("amro.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.amro.feature.details"
}

dependencies {
    implementation(projects.application.movie)
    implementation(projects.domain.movie)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
}
