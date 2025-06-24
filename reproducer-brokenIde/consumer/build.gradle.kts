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
