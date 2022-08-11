@file:Suppress("UnstableApiUsage")

package com.fleshgrinder.gradle.distribution.kit

import java.util.*
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.internal.util.PropertiesUtils
import org.gradle.work.DisableCachingByDefault

/**
 * Compiles the custom Gradle distribution.
 */
@DisableCachingByDefault(because = "Not worth caching.")
public abstract class CompileGradleDistribution @Inject constructor(
    @get:Nested public val config: GradleDistributionKitExtension,
    private val archives: ArchiveOperations,
    private val fs: FileSystemOperations,
) : DefaultTask() {
    init {
        group = GRADLE_DISTRIBUTION_TASK_GROUP
        description = "Compiles the custom Gradle distribution."
    }

    /**
     * Provides the ZIP archive with the latest Gradle release to compile the custom distribution with.
     */
    @get:InputFile
    public abstract val zip: RegularFileProperty

    /**
     * Provides the init scripts to include in the custom Gradle distribution.
     *
     * @see GradleDistributionKitExtension.sourceDirectories
     */
    @get:InputFiles
    public val initScripts: FileTree = config.sourceDirectories.asFileTree.matching {
        include(
            "**/*.init.gradle",
            "**/*.init.gradle.kts",
        )
    }

    /**
     * Provides the `gradle.properties` files of this project and the root project (if this is a subproject). This has
     * to be collected in a dedicated property for configuration cache compatibility, however, the files are considered
     * as an input to the task only if [GradleDistributionKitExtension.includeProjectGradleProperties] is set to `true`.
     *
     * @see gradleProperties
     */
    @get:Internal
    protected val projectGradleProperties: FileTree = project.files(project.rootDir, project.projectDir).asFileTree.matching {
        include("gradle.properties")
    }

    /**
     * Provides the Gradle properties files to include in the custom Gradle distribution.
     *
     * @see GradleDistributionKitExtension.sourceDirectories
     * @see projectGradleProperties
     */
    @get:InputFiles
    public val gradleProperties: Provider<FileTree> = config.includeProjectGradleProperties.map { includeProjectGradleProperties ->
        var files = config.sourceDirectories.asFileTree.matching {
            include(
                "**/gradle.properties",
                "**/*.gradle.properties",
            )
        }
        if (includeProjectGradleProperties) {
            files = files.plus(projectGradleProperties)
        }
        files
    }

    /**
     * Provides the directory where the custom Gradle distribution is compiled to.
     */
    @get:OutputDirectory
    public val destinationDirectory: Provider<Directory> = config.buildDirectory.zip(config.vendor) { buildDirectory, vendor ->
        buildDirectory.dir(vendor)
    }

    /**
     * Compiles the custom Gradle distribution.
     */
    @TaskAction
    public fun execute() {
        val config = config
        val destinationDirectory = destinationDirectory.get().asFile

        fs.delete {
            delete(destinationDirectory)
        }

        fs.copy {
            duplicatesStrategy = DuplicatesStrategy.FAIL
            includeEmptyDirs = false
            from(archives.zipTree(zip)) {
                eachFile { path = relativePath.segments.drop(1).joinToString("/") }
            }
            from(initScripts) {
                into("init.d")
            }
            into(destinationDirectory)
        }

        PropertiesUtils.store(
            Properties().also { properties ->
                gradleProperties.get().forEach { file ->
                    file.bufferedReader().use(properties::load)
                }
                config.gradleProperties.forEach { (k, v) ->
                    properties[k] = when (v) {
                        is Provider<*> -> v.get()
                        else -> v
                    }?.toString() ?: ""
                }
                properties[VENDOR_PROPERTY_NAME] = config.vendor.get()
            },
            destinationDirectory.resolve("gradle.properties"),
            null,
            Charsets.UTF_8,
            "\n",
        )
    }
}
