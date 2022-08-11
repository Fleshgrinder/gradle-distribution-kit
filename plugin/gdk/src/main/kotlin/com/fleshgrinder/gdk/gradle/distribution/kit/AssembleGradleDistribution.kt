package com.fleshgrinder.gradle.distribution.kit

import org.gradle.api.file.DuplicatesStrategy.FAIL
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.ZipEntryCompression.DEFLATED
import org.gradle.work.DisableCachingByDefault

/**
 * Assembles the custom Gradle distribution into a ZIP file for publishing.
 */
@DisableCachingByDefault(because = "Not worth caching.")
@Suppress("LeakingThis")
public abstract class AssembleGradleDistribution : Zip() {
    init {
        group = GRADLE_DISTRIBUTION_TASK_GROUP
        description = "Assembles the custom Gradle distribution into a ZIP file for publishing."
        duplicatesStrategy = FAIL
        entryCompression = DEFLATED
        includeEmptyDirs = false
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        isZip64 = false
        setMetadataCharset("UTF-8")
    }
}
