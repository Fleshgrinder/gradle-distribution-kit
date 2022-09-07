package com.fleshgrinder.gdk.github;

import com.fleshgrinder.gdk.GdkPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class GdkGithubPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        final TaskContainer tasks = project.getTasks();
        final TaskProvider<Task> publishTask = tasks.register(PublishGradleDistributionToGithub.NAME, task -> {
            task.setDescription("Publishes the custom Gradle Distribution produced by this project to GitHub.");
            task.setGroup(PublishingPlugin.PUBLISH_TASK_GROUP);
        });

        project.getPlugins().apply(GdkPlugin.class);
    }
}
