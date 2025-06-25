Starting in 8.12.0-alpha05, having the following stanza:
```
android {
    namespace = "omar.broken.producer"
    compileSdk = 36
    testOptions.unitTests.isIncludeAndroidResources = true
}
```


and running `./gradlew :producer:processDebugUnitTestManifest` causes a warning that didn't exist before:
```
package="omar.broken.producer" found in source AndroidManifest.xml: /Users/omarismail/Desktop/brokenStudio/reproducer-brokenMergeManifest/producer/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml.
Setting the namespace via the package attribute in the source AndroidManifest.xml is no longer supported, and the value is ignored.
Recommendation: remove package="omar.broken.producer" from the source AndroidManifest.xml: /Users/omarismail/Desktop/brokenStudio/reproducer-brokenMergeManifest/producer/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml.

```