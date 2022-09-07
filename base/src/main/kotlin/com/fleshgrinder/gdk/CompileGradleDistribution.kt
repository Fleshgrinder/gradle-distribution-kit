package com.fleshgrinder.gdk

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitResult.CONTINUE
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardOpenOption.CREATE_NEW
import java.nio.file.StandardOpenOption.READ
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipInputStream

public abstract class CompileGradleDistribution : GdkAbstractTask(
    "Compiles the custom Gradle distribution.",
) {
    @get:Nested
    public lateinit var extension: GdkExtension

    @get:InputFile
    public abstract val gradleZip: RegularFileProperty

    @TaskAction
    public fun execute() {
        val gradleZip = gradleZip.get().asFile.toPath()
        val outputDir = extension.outputDir.get().asFile.toPath()
        val files = extension.files.map(File::toPath)

        delete(outputDir)
        extract(gradleZip, outputDir)
        copy(files, outputDir)
    }

    public companion object {
        public const val NAME: String = "compileGradleDistribution"
    }
}

internal fun delete(dir: Path) {
    Files.newDirectoryStream(dir).use { entries ->
        val recursiveDelete = object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                Files.deleteIfExists(file)
                return CONTINUE
            }

            override fun postVisitDirectory(dir: Path, e: IOException?): FileVisitResult {
                if (e != null) throw e
                Files.deleteIfExists(dir)
                return CONTINUE
            }
        }

        entries.forEach { entry ->
            Files.walkFileTree(entry, recursiveDelete)
        }
    }
}

internal fun extract(zip: Path, target: Path) {
    ZipInputStream(Files.newInputStream(zip)).use { entries ->
        Channels.newChannel(entries).use { input ->
            while (true) {
                val entry = entries.nextEntry ?: break
                if (!entry.isDirectory) {
                    val path = target.resolve(entry.name)
                    Files.createDirectories(path.parent)
                    FileChannel.open(path, CREATE_NEW).use { output ->
                        output.transferFrom(input, 0, entry.size)
                    }
                }
            }
        }
    }
}

internal fun copy(files: Collection<Path>, target: Path) {
    val initPath = Files.createDirectories(target.resolve("init.d"))
    val propertiesPath = target.resolve("gradle.properties")
    var pos = 0L

    FileChannel.open(propertiesPath, CREATE_NEW).use { properties ->
        files.forEach { file ->
            FileChannel.open(file, READ).use { input ->
                val filename = file.fileName.toString()

                if (filename.contains(".init.gradle")) {
                    FileChannel.open(initPath.resolve(filename), CREATE_NEW).use { output ->
                        output.transferFrom(input, 0, Long.MAX_VALUE)
                    }
                } else if (filename.endsWith(".properties")) {
                    pos += properties.transferFrom(input, pos, Long.MAX_VALUE)
                }
            }
        }
    }

    if (pos == 0L) {
        Files.delete(propertiesPath)
    }
}
