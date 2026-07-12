import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("amro.android.library")
        pluginManager.apply("amro.compose")
    }
}
