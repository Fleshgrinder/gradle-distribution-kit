package com.fleshgrinder.gdk

import org.gradle.api.tasks.bundling.Zip
import javax.inject.Inject

public abstract class AssembleGradleDistribution @Inject constructor(

) : Zip(), GdkTask {
    init {
        group = GdkTask.GROUP_NAME
        description = "Assembles the custom Gradle distribution ZIP."
    }
}
