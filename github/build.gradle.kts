@file:Suppress("UnstableApiUsage")

plugins {
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    api(project(":base"))
}

gradlePlugin {
    plugins {
        register("") {
            id = "${project.group}.${project.name}"
            implementationClass = "${project.group}.github.GdkGitHubPlugin"
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    consistentResolution {
        useCompileClasspathVersions()
    }
}
