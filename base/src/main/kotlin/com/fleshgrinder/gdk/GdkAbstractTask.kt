package com.fleshgrinder.gdk

import org.gradle.api.DefaultTask
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
public abstract class GdkAbstractTask(description: String) : DefaultTask(), GdkTask {
    init {
        this.group = GdkTask.GROUP_NAME
        this.description = description
    }

    public companion object
}
