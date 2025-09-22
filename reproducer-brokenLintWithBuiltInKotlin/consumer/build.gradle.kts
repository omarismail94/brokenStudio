plugins {
    id("com.android.library")
    id("com.android.experimental.built-in-kotlin")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.consumer"
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

    lint {
        error.addAll( listOf("ComposableNaming"))
    }
}

androidComponents {
    beforeVariants(selector().withBuildType("debug")) { variant -> variant.enable = false }
    beforeVariants(selector().withBuildType("release")) { variant ->
        variant.enableAndroidTest = true
    }
}


dependencies {
    implementation(libs.runtime)
    implementation(libs.glance)
    implementation(project(":producer"))
    testImplementation(libs.junit)
}
