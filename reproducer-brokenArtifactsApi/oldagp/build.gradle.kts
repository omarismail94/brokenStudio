@file:OptIn(ExperimentalStdlibApi::class)

import omar.plugins.packageInspector

plugins {
    id("maven-publish")
    id("com.android.library")
}

group = "omar.broken"
version = "1.0"

packageInspector(project, ":producer")

android {
    namespace = "omar.broken.oldagp"
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
