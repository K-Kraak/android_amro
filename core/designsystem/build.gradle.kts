plugins { id("amro.android.library"); id("amro.compose") }
android { namespace = "com.amro.core.designsystem" }
dependencies { api(platform(libs.compose.bom)); api(libs.compose.material3); api(libs.compose.ui); implementation(libs.compose.ui.tooling.preview); debugImplementation(libs.compose.ui.tooling) }
