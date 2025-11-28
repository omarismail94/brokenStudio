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
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api("androidx.annotation:annotation:1.9.1")
            }
        }

        val commonJvmAndroidMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                api("androidx.collection:collection:1.5.0")
            }
        }

        jvmMain {
            dependsOn(commonJvmAndroidMain)
        }

        androidMain {
            dependsOn(commonJvmAndroidMain)
            dependencies {
                api("androidx.graphics:graphics-path:1.1.0-alpha01")
            }
        }
    }
}
