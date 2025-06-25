plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "omar.broken.producer"
    compileSdk = 36
    testOptions.unitTests.isIncludeAndroidResources = true
}
