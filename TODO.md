# TODO

- Advertise @ https://github.com/gradle/gradle/issues/17826

## Publishing Tasks with Auto-versioning

```kotlin
plugins {
    id("com.fleshgrinder.gradle.distribution.kit")
    id("com.fleshgrinder.gradle.distribution.github-publish")
    id("com.fleshgrinder.gradle.distribution.artifactory-publish")
}

gradleDistribution {
    publishing {
        // if not true what is the alternative?
        // with both github and artifactory, which one dictates the version?
        // we definitely have to avoid drift between the systems, maybe whatever
        // comes first? would have precedence with repository definitions
        autoVersioning.set(true)
        github {
            repository.set("owner/name")
        }
        artifactory {
            repository("https://jfrog/artifactory/gradle-distribution") {
                username.set(providers.environmentVariable("JFROG_USERNAME"))
                password.set(providers.environmentVariable("JFROG_PASSWORD"))
                token.set(providers.environmentVariable("JFROG_TOKEN"))
            }
        }
    }
}
```

```shell
./gradlew publishGradleDistroToArtifactory
./gradlew publishGradleDistroToGitHub
./gradlew publishGradleDistro
./gradlew publish
```

## `./gradlew selfupdate`

Ability to automatically update Gradle. This task should fetch the latest
custom distribution, but also directly install it.

### Create Pull Request

Ability to extend `selfupdate` with automatic Pull Request creation.

```kotlin
gradleDistribution {
    selfupdate {
        pullRequest {
            github {
                // choose sensible defaults
                enabledIf {
                    environmentVariable.set("GITHUB_ACTIONS")
                    equals.set("true")
                }
                title.set("Update Gradle to {version}")
                title { // DSL!
                    "Update Gradle to $version"
                }
                description.set(
                    """
                    Update Gradle to {version}, see [our release notes](https://github.com/fleshgrinder/custom-gradle-distribution/release/v{version}) as well
                    as [Gradle’s release notes](https://docs.gradle.org/{gradleVersion}/release-notes.html).
                    """.trimIndent()
                )
                description { // DSL!
                    """
                    Update Gradle to $version, see [our release notes](https://github.com/fleshgrinder/custom-gradle-distribution/release/v$version) as well
                    as [Gradle’s release notes](https://docs.gradle.org/$gradleVersion/release-notes.html).
                    """.trimIndent()
                }
                branchPrefix.set("KT-5")
                labels.add("🤖 autoupdate")
                assignees.add("@Fleshgrinder")
                // ...
            }

            gitlab { TODO() }
            bitbucket { TODO() }
            // ...
        }
    }
}
```

## Custom Gradle Wrapper Scripts

Ability to generate a modern wrapper script with support for tools that provide
the ability to auto-select the JDK.

```kotlin
gradleDistribution {
    wrapper {
        modern.set(true)
        jenv.set(true)
        sdkman.set(true)
    }
}
```

## `repositories.init.gradle.kts`

> **Note** how does this make anything simpler? Instead of this we could also
> simply provide extension functions for `RepositoryHandler` to configure
> additional repositories. However, this would introduce a dependency for the
> init script. Well, something we might need in any event, since cramping
> everything (see ideas above, e.g. `selfupdate`) into the init scripts is not
> a good idea.

- Separate file to isolate repository configuration.
- Extension allows addition of repositories, order is defined by position, can
  be enabled or disabled by default.
  ```kotlin
  gradleDistribution {
      repositories {
          centralReleases.enabled()     // repository.central.releases.enabled=true
          centralSnapshots.disabled()   // repository.central.snapshots.enabled=false
          gradlePluginPortal.enabled()  // repository.gradlePluginPortal.enabled=true
          confluent.enabled()           // repository.confluent.enabled=true

          maven("") {                   // repository.artifactory.releases.enabled=true
              id = "artifactory.releases"
              name = "HelloFresh Artifactory Releases"
              enabled()
              // allow everything just like Gradle does
          }
          maven("") {                   // repository.artifactory.snapshots.enabled=false
              id = "artifactory.snapshots"
              name = "HelloFresh Artifactory Snapshots"
              enabled()
              // allow everything just like Gradle does
          }

          // mavenLocal.disabled() is always last
          // repository.mavenLocal.enabled=false
      }
  }
  ```
