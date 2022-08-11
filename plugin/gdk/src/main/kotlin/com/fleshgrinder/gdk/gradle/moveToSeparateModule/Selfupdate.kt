package com.fleshgrinder.gradle.moveToSeparateModule

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
public class Selfupdate : DefaultTask() {
    @TaskAction
    public fun execute() {
        TODO("get latest version, start installation if new available")
    }
}
