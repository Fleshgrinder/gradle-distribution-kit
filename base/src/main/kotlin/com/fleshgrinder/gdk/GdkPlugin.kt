@file:Suppress("UnstableApiUsage")

package com.fleshgrinder.gdk

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.TEST_TASK_NAME
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin.ASSEMBLE_TASK_NAME

public abstract class GdkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<GdkExtension>(GdkExtension.NAME)
        val dependencies = target.dependencies

        val config = target.configurations.detachedConfiguration(
            dependencies.create("gradle:bin:+@zip")
        )

        target.repositories.ivy("https://services.gradle.org/distributions/") {
            content { onlyForConfigurations(config.name) }
            metadataSources { artifact() }
            patternLayout { artifact("[organization]-[revision]-[artifact].[ext]") }
        }

        val tasks = target.tasks

        val compileGradleDistribution by tasks.registering(CompileGradleDistribution::class) {
            this.extension = extension
            gradleZip.fileProvider(
                config.incoming.artifacts.resolvedArtifacts.map {
                    it.iterator().next().file
                }
            ).disallowUnsafeRead()
        }

        val assembleGradleDistribution by tasks.registering(AssembleGradleDistribution::class) {
            dependsOn(compileGradleDistribution)
        }

        target.apply<LifecycleBasePlugin>()
        tasks.named(ASSEMBLE_TASK_NAME) {
            dependsOn(assembleGradleDistribution)
        }

        target.pluginManager.withPlugin("java") {
            dependencies.add(
                TEST_IMPLEMENTATION_CONFIGURATION_NAME,
                dependencies.testFixtures("$GROUP:$ARTIFACT:$VERSION")
            )

            tasks.named<Test>(TEST_TASK_NAME) {
                dependsOn(compileGradleDistribution)
                systemProperty(OUTPUT_DIR_PROPERTY, extension.outputDir.get().asFile.absolutePath)
            }
        }
    }

    public companion object {
        @JvmStatic
        public val OUTPUT_DIR_PROPERTY: String = "${GdkPlugin::class.java.getPackage().name}.outputDir"

        @JvmStatic
        public val GROUP: String

        @JvmStatic
        public val ARTIFACT: String

        @JvmStatic
        public val VERSION: String

        init {
            val pkg = this::class.java.getPackage()
            GROUP = pkg.implementationVendor
            ARTIFACT = pkg.implementationTitle
            VERSION = pkg.implementationVersion
        }
    }
}
