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
}

dependencies {
    implementation(libs.androidGradlePluginApi)
    implementation(libs.kotlinGradlePlugin)
    runtimeOnly(libs.androidLibraryPlugin)
    runtimeOnly(libs.androidKotlinMultiplatform)
}
