package omar.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Attribute

@ExperimentalStdlibApi
fun packageInspector(libraryProject: Project, inspectorProjectPath: String) {
    val inspectorProject = libraryProject.rootProject.findProject(inspectorProjectPath)
    if (inspectorProject == null) {
        val extraProperties = libraryProject.extensions.extraProperties
        check(
            extraProperties.has("androidx.studio.type") &&
                    extraProperties.get("androidx.studio.type") == "playground"
        ) {
            "Cannot find $inspectorProjectPath. This is optional only for playground builds."
        }
        return
    }
    val consumeInspector = libraryProject.createConsumeInspectionConfiguration()
    libraryProject.dependencies.add(consumeInspector.name, inspectorProject)
    val consumeInspectorFiles = consumeInspector.incoming.artifactView {}.files
    libraryProject.extensions.getByType(AndroidComponentsExtension::class.java).onVariants { variant
        ->
        val taskProvider =
            libraryProject.tasks.register(
                variant.taskName("repackageAarWithInspectorJarFor"),
                AddInspectorJarToAarTask::class.java,
            ) { task ->
                task.from(consumeInspectorFiles) { it.rename { "inspector.jar" } }
                task.from(libraryProject.zipTree(task.aarFile))
                task.destinationDirectory.fileProvider(
                    task.output.locationOnly.map { location -> location.asFile.parentFile }
                )
                task.archiveFileName.set(
                    task.output.locationOnly.map { location -> location.asFile.name }
                )
            }
        variant.artifacts
            .use(taskProvider)
            .wiredWithFiles(AddInspectorJarToAarTask::aarFile, AddInspectorJarToAarTask::output)
            .toTransform(SingleArtifact.AAR)
    }
}

fun Project.createConsumeInspectionConfiguration(): Configuration =
    configurations.create("consumeInspector") {
        it.setupInspectorAttribute()
        it.isCanBeConsumed = false
    }

private fun Configuration.setupInspectorAttribute() {
    attributes { it.attribute(Attribute.of("inspector", String::class.java), "inspectorJar") }
}

private fun Variant.taskName(baseName: String) =
    "$baseName${name.replaceFirstChar(Char::titlecase)}"
