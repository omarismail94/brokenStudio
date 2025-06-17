import org.gradle.kotlin.dsl.register

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

group = "omar.broken"
version = "1.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
            }
        }
    }
}

android {
    namespace = "omar.broken.producer"
    compileSdk = 36
    testBuildType = "release"

    publishing {
        singleVariant("release") {
            withSourcesJar()
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
        variant -> variant.sources.res?.addGeneratedSourceDirectory(copyPublicResourcesDirTask, CopyPublicResourcesDirTask::outputFolder)
    }
}
