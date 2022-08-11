package com.fleshgrinder.gradle.distribution.kit

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat
import strikt.assertions.*

private class GradleDistributionKitTest {
    private lateinit var _gradle: GradleRunner
    private val gradle get() = _gradle
    private val settingsScript get() = gradle.projectDir.resolve("build/settings.gradle.kts")
    private val buildScript get() = gradle.projectDir.resolve("build/build.gradle.kts")
    private val gradleProperties get() = gradle.projectDir.resolve("gradle.properties")

    @BeforeEach
    fun prepareGradleBuild(@TempDir projectDir: File) {
        _gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir).forwardOutput()
        File(checkNotNull(this::class.java.getResource("/scaffold")) {
            "Could not find '/scaffold' directory in resources"
        }.toURI()).copyRecursively(gradle.projectDir)
    }

    @Test fun `fails if vendor is not configured`() {
        expectThat(gradle.withArguments("assemble").buildAndFail().output)
            .contains("property 'config.vendor' doesn't have a configured value.")
    }

    @Test fun `succeeds if vendor is configured`() {
        gradle.withArguments("-P${VENDOR_PROPERTY_NAME}=test", "assemble").build()
        expectThat(gradle.projectDir.resolve("build").listFiles()).isNotNull().toList().one {
            get { name } matches """\Atest-\d+(\.\d+)+-bin\.zip\z""".toRegex()
        }
    }
}
