### Issue: Incorrect Dependencies in shared JVM/Android source sets when `android.kmp.use.jvm.platform.type=false`

**Description:**
When `android.kmp.use.jvm.platform.type` is set to `false` in a project containing a shared source set between JVM and Android (e.g., `commonJvmAndroidMain`), two specific issues occur:

1. Dependencies from the shared source set incorrectly appear in the `metadataApiElements` variant within the `kotlinMultiplatform` module/POM.
2. These dependencies are also incorrectly included in the generated source JAR.

**Reproduction:**
Run the following command to observe the artifacts:
```bash
./gradlew generateMetadataFileForKotlinMultiplatformPublication sourceJar
````

and view the `consumer/build/module.json` and `consumer/build/libs/consumer-kotlin-1.0-sources.jar`
to see the dependencies from `commonJvmAndroidMain` in the `metadataApiElements`, and the source file
under that source set in the source jar.

**Suspected Cause:**
The flag changes the `KotlinPlatformType` for the Android publication from `jvm` to `androidJvm`. 
It appears the Kotlin Gradle Plugin logic does not account for this type change in shared source sets.

**Expected behaviour:**
For there to be no diff from when `android.kmp.use.jvm.platform.type=true`