plugins {
    id("amro.kotlin.library")
}
dependencies {
    api(projects.domain.movie)
    api(projects.core.common)
    api(libs.androidx.paging.common)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(projects.core.testing)
}
