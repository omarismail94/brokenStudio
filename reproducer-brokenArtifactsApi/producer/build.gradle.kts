import omar.plugins.packageInspector

plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

group = "omar.broken"
version = "1.0"

configurations.register("inspectorJarProducer") {
    isCanBeConsumed = true
    isCanBeResolved = false

    attributes {
        attribute(Attribute.of("inspector", String::class.java), "inspectorJar")
    }
}

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

androidComponents {
    beforeVariants(selector().withBuildType("debug")) { variant -> variant.enable = false }
}
