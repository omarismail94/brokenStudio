buildscript {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlinGradlePlugin)
    }
}

apply(plugin = "kotlin")

dependencies {
    compileOnly(project.gradleKotlinDsl())
    implementation(libs.androidGradlePluginApi)
    implementation(libs.kotlinGradlePlugin)
    runtimeOnly(libs.androidLibraryPlugin)
    runtimeOnly(libs.androidKotlinMultiplatform)
}
