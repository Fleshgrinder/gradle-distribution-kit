plugins {
    `kotlin-dsl`
}

gradlePlugin.plugins.register("") {
    id = "com.fleshgrinder.gradle-distribution"
    implementationClass = "com.fleshgrinder.gradle.distribution"
}

kotlin.explicitApi()
