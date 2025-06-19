@file:OptIn(ExperimentalStdlibApi::class)

import omar.plugins.packageInspector

plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

group = "omar.broken"
version = "1.0"

packageInspector(project, ":producer")

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

