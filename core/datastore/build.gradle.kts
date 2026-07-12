plugins { id("amro.android.library") }
android { namespace = "com.amro.core.datastore" }
dependencies { implementation(libs.androidx.datastore.preferences); implementation(libs.kotlinx.coroutines.core) }
