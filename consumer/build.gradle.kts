import com.android.build.api.dsl.KotlinMultiplatformAndroidDeviceTestCompilation

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

group = "omar.broken"
version = "1.0"

kotlin {
    androidLibrary {
        namespace = "omar.broken.consumer"
        compileSdk = 36
        minSdk = 24
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
