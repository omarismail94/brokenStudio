plugins {
    id("com.android.library")
    id("com.android.experimental.built-in-kotlin")
    // If you comment out the line above, and uncomment the line below, the :consumer:lintRelease task passes
    // id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.producer"
    compileSdk = 36

    buildFeatures {
        compose = true
    }
    defaultConfig {
     minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

androidComponents {
    beforeVariants(selector().withBuildType("debug")) { variant -> variant.enable = false }
}


dependencies {
    implementation(libs.runtime)
    implementation(libs.glance)
    testImplementation(libs.junit)
}
