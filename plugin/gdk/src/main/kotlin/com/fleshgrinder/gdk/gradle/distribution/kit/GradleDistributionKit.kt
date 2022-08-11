package com.fleshgrinder.gradle.distribution.kit

import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

/**
 * Gets the name of the task group for all Gradle distribution tasks.
 */
public const val GRADLE_DISTRIBUTION_TASK_GROUP: String = "gradle distribution"

/**
 * Gets the name of the [GradleDistributionKitExtension] with which the [GradleDistributionKitPlugin] registers it on
 * the project the plugin is applied to.
 */
public const val GRADLE_DISTRIBUTION_EXTENSION_NAME: String = "gradleDistribution"

/**
 * Gets the name of the [AssembleGradleDistribution] task with which the [GradleDistributionKitPlugin] registers it on
 * the project the plugin is applied to.
 */
public const val ASSEMBLE_GRADLE_DISTRIBUTION_TASK_NAME: String = "assembleGradleDistribution"

/**
 * Gets the name of the [CompileGradleDistribution] task with which the [GradleDistributionKitPlugin] registers it on
 * the project the plugin is applied to.
 */
public const val COMPILE_GRADLE_DISTRIBUTION_TASK_NAME: String = "compileGradleDistribution"

/**
 * Gets the [DistributionType] name that is resolved, compiled, and assembled. This is hardcoded to the
 * [DistributionType.BIN] type, because repackaging the [DistributionType.ALL] type makes no sense. IntelliJ
 * automatically downloads the Gradle sources whenever they are required, regardless of the chosen distribution type
 * (see [IDEA-203597](https://youtrack.jetbrains.com/issue/IDEA-203597)), and users who only want to execute a build
 * suffer from extended download times if [DistributionType.ALL] is used instead of [DistributionType.BIN].
 */
public const val GRADLE_DISTRIBUTION_TYPE: String = "bin"

/**
 * Gets the default Ivy artifact pattern to match the Gradle distributions from the [DEFAULT_REPOSITORY_URL].
 *
 * @see GradleDistributionKitExtension.artifactPattern
 */
public const val DEFAULT_ARTIFACT_PATTERN: String = "[organization]-[revision]-[artifact].[ext]"

/**
 * Gets the default name for the directory within the [build directory][ProjectLayout.getBuildDirectory] where the
 * custom Gradle distribution gets [compiled][CompileGradleDistribution] to.
 *
 * @see GradleDistributionKitExtension.buildDirectory
 */
public const val DEFAULT_BUILD_DIRECTORY_NAME: String = "gradle-distribution"

/**
 * Gets the default Ivy repository URL to resolve Gradle from for compiling of the custom distribution.
 *
 * @see GradleDistributionKitExtension.repositoryUrl
 */
public const val DEFAULT_REPOSITORY_URL: String = "https://services.gradle.org/distributions/"

/**
 * Gets the name of the default source set that is used to collect init scripts and Gradle properties files. Defaults
 * to [SourceSet.MAIN_SOURCE_SET_NAME].
 *
 * @see GradleDistributionKitExtension.sourceDirectories
 */
public const val DEFAULT_SOURCE_SET_NAME: String = SourceSet.MAIN_SOURCE_SET_NAME

private const val PROPERTY_PREFIX = "com.fleshgrinder.gradle.distribution."

/**
 * Gets the name of the Gradle property with which the name of the vendor of the custom Gradle distribution can be
 * configured.
 *
 * @see GradleDistributionKitExtension.vendor
 */
public const val VENDOR_PROPERTY_NAME: String = PROPERTY_PREFIX + "vendor"
