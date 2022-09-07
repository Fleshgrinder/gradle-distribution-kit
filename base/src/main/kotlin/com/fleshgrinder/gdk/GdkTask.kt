package com.fleshgrinder.gdk

import org.gradle.api.Task

public interface GdkTask : Task {
    public companion object {
        public const val GROUP_NAME: String = "gradle distribution"
    }
}
