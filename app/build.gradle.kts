plugins {
    id("amro.android.application")
    id("amro.compose")
    id("amro.hilt")
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.amro.app"
    defaultConfig {
        applicationId = "com.amro.app";
        versionCode = 1;
        versionName = "1.0.0"
    }
}
dependencies {
    implementation(projects.feature.trending)
    implementation(projects.domain.movie)
    implementation(projects.data.movie)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.dispatchers)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}
