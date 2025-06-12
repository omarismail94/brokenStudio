import com.android.build.api.dsl.KotlinMultiplatformAndroidDeviceTestCompilation

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()

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
}
