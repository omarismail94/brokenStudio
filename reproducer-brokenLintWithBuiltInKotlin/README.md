# Bug Report: `ComposableNaming` Lint Error with Built-in Kotlin Plugin

This project reproduces a bug where the `:consumer:lintRelease` task fails with a `ComposableNaming` lint error when a `:producer` module uses the built-in Kotlin plugin. The lint task passes when the `:producer` module uses the `kotlin-android` plugin instead.

## The Bug

The following lint error occurs in the `:consumer` module:

```
Error: Composable functions with a return type should start with a lowercase letter [ComposableNaming from androidx.compose.runtime]
 @Composable private fun Space() = MySpacer(GlanceModifier.size(16.dp))
```


## Steps to Reproduce

1.  Clone this repository.
2.  Run the `:consumer:lintRelease` Gradle task:
    ```bash
    ./gradlew :consumer:lintRelease
    ```

## Expected Behavior

The `:consumer:lintRelease` task should complete successfully without any lint errors.

## Actual Behavior

The `:consumer:lintRelease` task fails with the `ComposableNaming` lint error mentioned above.

## Workaround

The lint error can be avoided by applying the `org.jetbrains.kotlin.android` plugin to the `:producer` module's `build.gradle.kts` file, instead of `com.android.experimental.built-in-kotlin`.
