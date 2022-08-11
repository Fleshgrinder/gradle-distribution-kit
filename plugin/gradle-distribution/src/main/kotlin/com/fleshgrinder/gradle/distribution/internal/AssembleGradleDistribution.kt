package com.fleshgrinder.gradle.distribution.internal

import org.gradle.api.file.DuplicatesStrategy.FAIL
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.ZipEntryCompression.DEFLATED
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
public abstract class AssembleGradleDistribution : Zip() {
    init {
        group = "gradle distribution"
        description = "Assembles the custom Gradle distribution into a ZIP file for publishing."
        duplicatesStrategy = FAIL
        entryCompression = DEFLATED
        includeEmptyDirs = false
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        isZip64 = false
        metadataCharset
        setMetadataCharset("UTF-8")
    }
}
