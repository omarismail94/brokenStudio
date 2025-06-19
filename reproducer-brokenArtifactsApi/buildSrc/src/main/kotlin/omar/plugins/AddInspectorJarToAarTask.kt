package omar.plugins

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.bundling.Zip
import org.gradle.work.DisableCachingByDefault

/** Task to facilitate repackaging an [aarFile] and adding additional files */
@DisableCachingByDefault(because = "Not worth caching")
abstract class AddInspectorJarToAarTask : Zip() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val aarFile: RegularFileProperty

    @get:OutputFile
    abstract val output: RegularFileProperty

    override fun copy() {
        println("I am HERE!!!")
        super.copy()
    }
}
