plugins {
    id("amro.android.library");
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.amro.core.database"
}
dependencies {
    api(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
}
ksp {
    arg("room.schemaLocation",
        "$projectDir/schemas"
    )
}
