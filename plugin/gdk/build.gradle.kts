plugins {
    `kotlin-dsl`
    `maven-publish`
}

gradlePlugin.plugins.register("") {
    id = "${project.group}"
    implementationClass = "${project.group}.GdkPlugin"
}

kotlin.explicitApi()
