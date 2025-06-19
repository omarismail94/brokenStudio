package omar.plugins

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.StopExecutionException
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
/**
 * A plugin which, when present, ensures that intermediate inspector resources are generated at
 * build time
 */
class InspectionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        var foundLibraryPlugin = false
        var foundReleaseVariant = false
        val extension = project.extensions.create<InspectionExtension>(EXTENSION_NAME, project)
        val publishInspector =
            project.configurations.create("publishInspector") {
                it.isCanBeConsumed = true
                it.isCanBeResolved = false
                it.setupInspectorAttribute()
            }
        val publishNonDexedInspector =
            project.configurations.create("publishNonDexedInspector") {
                it.isCanBeConsumed = true
                it.isCanBeResolved = false
                it.setupNonDexedInspectorAttribute()
            }
        project.configurations.create(EXPORT_INSPECTOR_DEPENDENCIES) {
            // to allow including these dependencies in an SBOM
            it.description = "Re-publishes dependencies of the inspector"
            it.isCanBeConsumed = true
            it.isCanBeResolved = true
            it.extendsFrom(project.configurations.getByName("implementation"))
            it.setupReleaseAttribute()
        }
        project.pluginManager.withPlugin("com.android.library") {
            foundLibraryPlugin = true
            val libExtension = project.extensions.getByType(LibraryExtension::class.java)
            val componentsExtension =
                project.extensions.findByType(AndroidComponentsExtension::class.java)
                    ?: throw GradleException("android plugin must be used")
//            componentsExtension.onVariants { variant: Variant ->
//                if (variant.name == "release") {
//                    foundReleaseVariant = true
//                    val unzip = project.registerUnzipTask(variant)
//                    val shadowJar =
//                        project.registerShadowDependenciesTask(variant, extension.name, unzip)
//                    val bundleTask =
//                        project.registerBundleInspectorTask(
//                            variant,
//                            libExtension,
//                            componentsExtension as LibraryAndroidComponentsExtension,
//                            extension.name,
//                            shadowJar,
//                        )
//                    publishNonDexedInspector.outgoing.variants {
//                        val configVariant = it.create("inspectorNonDexedJar")
//                        configVariant.artifact(shadowJar)
//                    }
//                    publishInspector.outgoing.variants {
//                        val configVariant = it.create("inspectorJar")
//                        configVariant.artifact(bundleTask)
//                    }
//                }
//            }
            libExtension.sourceSets.named("main").configure {
                it.resources.srcDirs(File(project.rootDir, "src/main/proto"))
                it.resources.srcDirs("META-INF/services")
            }
        }
        project.apply(plugin = "com.google.protobuf")
        project.dependencies { add("implementation", project.getLibraryByName("protobufLite")) }
        project.afterEvaluate {
            if (!foundLibraryPlugin) {
                throw StopExecutionException(
                    """A required plugin, com.android.library, was not found.
                        The androidx.inspection plugin currently only supports android library
                        modules, so ensure that com.android.library is applied in the project
                        build.gradle file."""
                        .trimIndent()
                )
            }
            if (!foundReleaseVariant) {
                throw StopExecutionException(
                    "The androidx.inspection plugin requires " + "release build variant."
                )
            }
        }
    }
}
private fun Project.getLibraryByName(name: String): MinimalExternalModuleDependency {
    val libs = project.extensions.getByType(VersionCatalogsExtension::class.java).find("libs").get()
    val library = libs.findLibrary(name)
    return if (library.isPresent) {
        library.get().get()
    } else {
        throw GradleException("Could not find a library for `$name`")
    }
}
/**
 * Use this function in [libraryProject] to include inspector that will be compiled into
 * inspector.jar and packaged in the library's aar.
 *
 * @param libraryProject project that is inspected and which aar will host inspector.jar . E.g.
 *   work-runtime
 * @param inspectorProjectPath project path of the inspector, that will be compiled into the
 *   inspector.jar. E.g. :work:work-inspection
 */
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
        // skip setting up inspector project
        return
    }
    val consumeInspector = libraryProject.createConsumeInspectionConfiguration()
    libraryProject.dependencies.add(consumeInspector.name, inspectorProject)
    val consumeInspectorFiles = consumeInspector.incoming.artifactView {}.files
//    libraryProject.extensions.getByType(AndroidComponentsExtension::class.java).onVariants { variant
//        ->
//        libraryProject.registerGenerateProguardDetectionFileTask(variant as LibraryVariant)
//        val taskProvider =
//            libraryProject.tasks.register(
//                variant.taskName("repackageAarWithInspectorJarFor"),
//                AddInspectorJarToAarTask::class.java,
//            ) { task ->
//                task.from(consumeInspectorFiles) { it.rename { "inspector.jar" } }
//                task.from(libraryProject.zipTree(task.aarFile))
//                task.destinationDirectory.fileProvider(
//                    task.output.locationOnly.map { location -> location.asFile.parentFile }
//                )
//                task.archiveFileName.set(
//                    task.output.locationOnly.map { location -> location.asFile.name }
//                )
//            }
//        variant.artifacts
//            .use(taskProvider)
//            .wiredWithFiles(AddInspectorJarToAarTask::aarFile, AddInspectorJarToAarTask::output)
//            .toTransform(SingleArtifact.AAR)
//    }
    libraryProject.configurations.create(IMPORT_INSPECTOR_DEPENDENCIES) {
        it.setupReleaseAttribute()
    }
    libraryProject.dependencies.add(
        IMPORT_INSPECTOR_DEPENDENCIES,
        libraryProject.dependencies.project(
            mapOf("path" to inspectorProjectPath, "configuration" to EXPORT_INSPECTOR_DEPENDENCIES)
        ),
    )
    // When adding package inspector to a new project, add the artifactId here
    // to ensure inspector.jar is packaged in the correct location
    val artifactId =
        when (libraryProject.name) {
            "ui" -> "ui-android"
            "work-runtime" -> "work-runtime"
            else ->
                throw GradleException(
                    "Project ${libraryProject.name} does not have artifactId defined " +
                            "for packaging the inspector.jar file"
                )
        }
//    libraryProject.createVerifyInspectorJarPresentTask(artifactId)
}
fun Project.createConsumeInspectionConfiguration(): Configuration =
    configurations.create("consumeInspector") {
        it.setupInspectorAttribute()
        it.isCanBeConsumed = false
    }
private fun Configuration.setupInspectorAttribute() {
    attributes { it.attribute(Attribute.of("inspector", String::class.java), "inspectorJar") }
}
fun Project.createConsumeNonDexedInspectionConfiguration(): Configuration =
    configurations.create("consumeNonDexedInspector") {
        it.setupNonDexedInspectorAttribute()
        it.isCanBeConsumed = false
    }
private fun Configuration.setupNonDexedInspectorAttribute() {
    attributes {
        it.attribute(Attribute.of("inspector-undexed", String::class.java), "inspectorUndexedJar")
    }
}
private fun Configuration.setupReleaseAttribute() {
    attributes {
        it.attribute(
            Attribute.of("com.android.build.api.attributes.BuildTypeAttr", String::class.java),
            "release",
        )
        it.attribute(
            Attribute.of("artifactType", String::class.java),
            ArtifactTypeDefinition.JAR_TYPE,
        )
    }
}
const val EXTENSION_NAME = "inspection"
const val EXPORT_INSPECTOR_DEPENDENCIES = "exportInspectorImplementation"
const val IMPORT_INSPECTOR_DEPENDENCIES = "importInspectorImplementation"
open class InspectionExtension(@Suppress("UNUSED_PARAMETER") project: Project) {
    /** Name of built inspector artifact, if not provided it is equal to project's name. */
    var name: String? = null
}
