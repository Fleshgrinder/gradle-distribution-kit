plugins {
    `kotlin-dsl`
    `maven-publish`
}

gradlePlugin.plugins.register("") {
    id = "${project.group}.${project.name}"
    implementationClass = "${project.group}.GithubAutoPublishPlugin"
}

kotlin.explicitApi()
