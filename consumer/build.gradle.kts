import com.android.build.api.dsl.KotlinMultiplatformAndroidDeviceTestCompilation

plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

group = "omar.broken"
version = "1.0"

kotlin {
    androidLibrary {
        namespace = "omar.broken.consumer"
        compileSdk = 36
        minSdk = 24
        androidResources.enable = true
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(project(":producer"))
            }
        }
    }
}

abstract class CopyPublicResourcesDirTask : DefaultTask() {

    @get:Inject abstract val fileSystemOperations: FileSystemOperations

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val buildSrcResDir: DirectoryProperty

    @get:OutputDirectory abstract val outputFolder: DirectoryProperty

    @TaskAction
    fun copy() {
        File(outputFolder.get().asFile.path).apply {
            deleteRecursively()
            mkdirs()
            fileSystemOperations.copy {
                from(buildSrcResDir)
                into(this@apply)
            }
        }
    }
}

val staticPublicXmlDirectory =  File(project.projectDir.parentFile , "buildSrc/res")

val copyPublicResourcesDirTask =
    project.tasks.register(
        "generatePublicResourcesStub",
        CopyPublicResourcesDirTask::class,
    ) {
        buildSrcResDir.set(staticPublicXmlDirectory)
    }

androidComponents {
    onVariants(selector().all()) {
//            variant -> variant.sources.res?.addStaticSourceDirectory(staticPublicXmlDirectory.absolutePath)
        variant -> variant.sources.res?.addGeneratedSourceDirectory(copyPublicResourcesDirTask, CopyPublicResourcesDirTask::outputFolder)
    }
}
