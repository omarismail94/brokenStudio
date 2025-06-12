import com.android.build.api.dsl.KotlinMultiplatformAndroidDeviceTestCompilation

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "omar.broken.producer"
        compileSdk = 36
        minSdk = 24
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
            }
        }
    }
}
