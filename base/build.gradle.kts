@file:Suppress("UnstableApiUsage")

import java.util.jar.Attributes.Name.*

plugins {
    `kotlin-dsl`
    `java-test-fixtures`
    `maven-publish`
}

dependencies {
    testFixturesApi(project)
    testFixturesApi(gradleTestKit())

    testImplementation(testFixtures(project))
    testImplementation("com.google.jimfs:jimfs:1.2")

    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

gradlePlugin {
    plugins {
        register("") {
            id = "${project.group}.${project.name}"
            implementationClass = "${project.group}.GdkPlugin"
        }
    }
}

kotlin {
    explicitApi()
}

java {
    withSourcesJar()
    withJavadocJar()
    consistentResolution {
        useCompileClasspathVersions()
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }

    compileTestFixturesKotlin {
        kotlinOptions {
            allWarningsAsErrors = true
            freeCompilerArgs = listOf(
                "-Xexplicit-api=strict",
            )
        }
    }

    jar {
        manifest {
            attributes[IMPLEMENTATION_TITLE.toString()] = project.name
            attributes[IMPLEMENTATION_VENDOR.toString()] = project.group.toString()
            attributes[IMPLEMENTATION_VERSION.toString()] = project.version.toString()
        }
    }
}
