package com.fleshgrinder.gradle.moveToSeparateModule

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.maven

// we would need multiple receiver support to get rid of providers 😢

public fun RepositoryHandler.mavenCentralReleases(providers: ProviderFactory) {
    if (providers.gradleProperty("repository.mavenCentral.releases.enabled").getOrElse("true") == "true") {
        mavenCentral {
            name = "Maven Central Releases"
        }
    }
}

public fun RepositoryHandler.mavenCentralSnapshots() {}

public fun RepositoryHandler.confluent(providers: ProviderFactory) {
    if (providers.gradleProperty("repository.confluent.enabled").getOrElse("true") == "true") {
        exclusiveContent {
            filter {
                includeGroupByRegex(/* language=regexp */ """\Aio\.confluent(?:\..+)?\z""")
            }
            forRepositories(maven("https://packages.confluent.io/maven/") {
                name = "Confluent Releases"
                mavenContent {
                    releasesOnly()
                    includeGroupByRegex(/* language=regexp */ """\Acom\.linkedin\.camus(?:\..+)?\z""")
                    includeGroupByRegex(/* language=regexp */ """\Aorg\.apache\.kafka(?:\..+)?\z""")
                }
                metadataSources.apply {
                    gradleMetadata()
                    mavenPom()
                }
            })
        }
    }
}

public fun RepositoryHandler.springMilestones() {}
public fun RepositoryHandler.springSnapshots() {}
