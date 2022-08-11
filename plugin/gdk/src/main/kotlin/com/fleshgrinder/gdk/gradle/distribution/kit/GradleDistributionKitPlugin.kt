@file:Suppress("UnstableApiUsage")

package com.fleshgrinder.gradle.distribution.kit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.*
import org.gradle.language.base.plugins.LifecycleBasePlugin.ASSEMBLE_TASK_NAME

/**
 * Configures a project for resolving, compiling, and assembling of a custom Gradle distribution.
 */
public abstract class GradleDistributionKitPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configuration = project.configurations.detachedConfiguration(project.dependencies.create("gradle:$GRADLE_DISTRIBUTION_TYPE:+@zip"))
        val artifact = configuration.incoming.artifacts.resolvedArtifacts.map { it.single() }
        val providers = project.providers
        val tasks = project.tasks

        val extension = project.extensions.create(GRADLE_DISTRIBUTION_EXTENSION_NAME, GradleDistributionKitExtension::class).apply {
            artifactPattern.apply {
                convention(DEFAULT_ARTIFACT_PATTERN)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            buildDirectory.apply {
                convention(project.layout.buildDirectory.dir(DEFAULT_BUILD_DIRECTORY_NAME))
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            /* TODO uncomment once MapProperty is usable in Gradle.
            gradleProperties.apply {
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            */
            includeProjectGradleProperties.apply {
                convention(false)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            preReleases.apply {
                convention(false)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            repositoryUrl.apply {
                convention(DEFAULT_REPOSITORY_URL)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            sourceDirectories.apply {
                project.apply<JavaPlugin>()
                from(project.extensions.getByType<SourceSetContainer>().named(DEFAULT_SOURCE_SET_NAME).map { it.allSource })
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            vendor.apply {
                convention(providers.gradleProperty(VENDOR_PROPERTY_NAME))
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
        }

        // We have to defer the configuration of these aspects to give the user a chance to configure these properties.
        // Not doing so would mean that the users cannot configure these properties programmatically, and it would lead
        // to an exception, because we are reading the properties before the configuration phase has concluded.
        (project as ProjectInternal).addDeferredConfiguration {
            if (!extension.preReleases.get()) {
                configuration.resolutionStrategy.componentSelection.all {
                    if ('-' in candidate.version) {
                        reject("Pre-release: ${candidate.version}")
                    }
                }
            }

            project.repositories.ivy(extension.repositoryUrl.get()) {
                content { onlyForConfigurations(configuration.name) }
                metadataSources.artifact()
                patternLayout { artifact(extension.artifactPattern.get()) }
            }
        }

        val compileTask = tasks.register<CompileGradleDistribution>(COMPILE_GRADLE_DISTRIBUTION_TASK_NAME, extension)
        compileTask.configure {
            zip.apply {
                fileProvider(artifact.map { it.file })
                disallowChanges()
                finalizeValueOnRead()
            }
        }

        val assembleTask = tasks.register<AssembleGradleDistribution>(ASSEMBLE_GRADLE_DISTRIBUTION_TASK_NAME) {
            dependsOn(compileTask)
            from(compileTask.flatMap { it.destinationDirectory })
            destinationDirectory.apply {
                convention(extension.buildDirectory)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            archiveBaseName.apply {
                convention(extension.vendor)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            archiveVersion.apply {
                convention(artifact.map { (it.variant.owner as ModuleComponentIdentifier).version })
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
            archiveClassifier.apply {
                convention(GRADLE_DISTRIBUTION_TYPE)
                disallowUnsafeRead()
                finalizeValueOnRead()
            }
        }

        tasks.named(ASSEMBLE_TASK_NAME) {
            dependsOn(assembleTask)
        }
    }
}
