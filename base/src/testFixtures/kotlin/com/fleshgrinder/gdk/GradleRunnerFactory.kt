@file:JvmName("GradleRunnerFactory")

package com.fleshgrinder.gdk

import com.fleshgrinder.gdk.GdkPlugin.Companion.OUTPUT_DIR_PROPERTY
import org.gradle.testkit.runner.GradleRunner
import java.io.File

private val INSTALLATION = File(checkNotNull(System.getProperty(OUTPUT_DIR_PROPERTY)) {
    "Missing required '$OUTPUT_DIR_PROPERTY' system property."
})

/**
 * Creates a new [GradleRunner] with the [installation][GradleRunner.withGradleInstallation]
 * set to the [custom distribution][OUTPUT_DIR_PROPERTY] that was built by the
 * [GdkPlugin].
 */
@JvmName("create")
public fun gradleRunnerOf(projectDir: File): GradleRunner =
    GradleRunner.create().withGradleInstallation(INSTALLATION).withProjectDir(projectDir)
