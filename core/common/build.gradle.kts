plugins {
    id("amro.kotlin.library")
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    testImplementation(libs.junit4)
}
