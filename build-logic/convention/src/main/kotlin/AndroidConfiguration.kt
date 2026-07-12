import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

internal fun CommonExtension.configureAndroid() {
    compileSdk = ProjectConfig.COMPILE_SDK

    defaultConfig.minSdk = ProjectConfig.MIN_SDK

    compileOptions.sourceCompatibility =
        JavaVersion.toVersion(ProjectConfig.JVM_VERSION)

    compileOptions.targetCompatibility =
        JavaVersion.toVersion(ProjectConfig.JVM_VERSION)

    testOptions.unitTests.isIncludeAndroidResources = true
}