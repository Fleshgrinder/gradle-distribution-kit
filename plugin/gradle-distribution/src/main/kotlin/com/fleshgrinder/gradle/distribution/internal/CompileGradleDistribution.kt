package com.fleshgrinder.gradle.distribution.internal

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
public abstract class CompileGradleDistribution : DefaultTask() {
    @get:OutputDirectory
    public abstract val destinationDirectory: DirectoryProperty

    @TaskAction
    public fun execute() {
        TODO("not implemented")
    }
}
