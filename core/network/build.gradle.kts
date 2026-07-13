plugins {
    id("amro.android.library")
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.amro.core.network"
}
dependencies {
    api(libs.retrofit.core)
    api(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
}
