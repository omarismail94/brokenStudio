plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

group = "omar.broken"
version = "1.0"

android {
    namespace = "com.example.library"
    compileSdk = 34
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
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
