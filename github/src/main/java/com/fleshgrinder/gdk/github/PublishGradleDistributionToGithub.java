package com.fleshgrinder.gdk.github;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault
public abstract class PublishGradleDistributionToGithub extends DefaultTask {
    public static final String NAME = "publishGradleDistributionToGithub";

    @InputDirectory
    public abstract RegularFileProperty getZipArchive();

    @TaskAction
    public void execute() {
        // TODO create GitHub release
        // TODO upload zip asset *
        // TODO upload sha256 asset *
        // TODO * possible with Gradle?
    }
}
