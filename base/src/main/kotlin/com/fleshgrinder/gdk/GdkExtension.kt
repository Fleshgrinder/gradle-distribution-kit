package com.fleshgrinder.gdk

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.*
import javax.inject.Inject

/**
 * The [GdkExtension] provides the configuration properties for the [GdkPlugin]
 * and its tasks.
 */
public abstract class GdkExtension @Inject constructor(
    layout: ProjectLayout,
    objects: ObjectFactory,
) : ExtensionAware {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:SkipWhenEmpty
    public val files: ConfigurableFileTree = objects.fileTree().apply {
        from("src/main/groovy")
        from("src/main/kotlin")
        from("src/main/resources")
        include("*.init.gradle", "*.init.gradle.kts", "*.properties")
    }

    /**
     * Specifies the directory where the custom Gradle distribution files are
     * being stored to. Defaults to `gdk` in the build directory of the current
     * project.
     */
    @get:OutputDirectory
    public val outputDir: DirectoryProperty = objects.directoryProperty().apply {
        convention(layout.buildDirectory.dir(NAME))
        disallowUnsafeRead()
    }

    public companion object {
        /**
         * Gets the name of the [GdkExtension] with which the extension gets
         * registered by the [GdkPlugin] if it is applied to a project.
         */
        public const val NAME: String = "gdk"
    }
}
