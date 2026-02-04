package com.zioanacleto.speakeazy.buildlogic

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

val coverageExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Activity*.*",
    "**/*Screen*.*",
    "**/*SpeakEazyApp*.*",
    "**/*SpeakEazyRoomDatabase*.*",
    "**/di/**",
    "**/theme/**",
    "**/components/**",
    "**/navigation/**",
    "**/logger/**",
    "**/steps/**",
    "**/*Cocktail3DScene*.*",
    "**/MainFilterItem.*"
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

/**
 *  Creates a new task that generates a JaCoCo coverage report.
 *  jacocoCoverage{variant}Report
 *  Note that coverage data must exist before running the task.
 *  This allows us to run device tests on CI using a different Github Action or an external device farm.
 */
internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    androidComponentsExtension.onVariants { variant ->
        val buildDir = layout.buildDirectory.get().asFile
        val allJars: ListProperty<RegularFile> =
            project.objects.listProperty(RegularFile::class.java)
        val allDirectories: ListProperty<Directory> =
            project.objects.listProperty(Directory::class.java)

        val task = tasks.register(
            name = "jacocoCoverage${variant.name.capitalize()}Report",
            type = JacocoReport::class
        ) {
            dependsOn("test${variant.name.capitalize()}UnitTest")

            classDirectories.setFrom(
                allDirectories.map { dirs ->
                    dirs.map { dir ->
                        project.objects.fileTree().setDir(dir).exclude(coverageExclusions)
                    }
                },
            )
            reports {
                xml.required = true
                html.required = true
            }

            sourceDirectories.setFrom(
                files(
                    "$projectDir/src/main/java"
                )
            )

            executionData.setFrom(
                fileTree(buildDir) {
                    include(
                        "jacoco/test${variant.name.capitalize()}UnitTest.exec",
                        "jacoco/*.exec"
                    )
                }
            )
        }

        variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
            .use(task)
            .toGet(
                ScopedArtifact.CLASSES,
                { _ -> allJars },
                { _ -> allDirectories },
            )
    }

    tasks.withType<Test> {
        testLogging {
            events(
                TestLogEvent.STARTED,
                TestLogEvent.PASSED,
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED
            )
            showStandardStreams = true
        }

        configure<JacocoTaskExtension> {
            // Required for JDK 11 with the above
            excludes = listOf("jdk.internal.*")
        }
    }
}