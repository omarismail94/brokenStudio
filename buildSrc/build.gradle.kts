plugins {
    `java-gradle-plugin`
}

dependencies {
    compileOnly(project.gradleKotlinDsl())
    implementation(libs.androidGradlePluginApi)
    implementation(libs.kotlinGradlePlugin)
    runtimeOnly(libs.androidLibraryPlugin)
    runtimeOnly(libs.androidKotlinMultiplatform)
}

gradlePlugin {
    plugins {
        create("inspection-plugin") {
            id = "omar.plugins.inspection-plugin"
            implementationClass = "omar.plugins.InspectionPlugin"
        }
    }
}

