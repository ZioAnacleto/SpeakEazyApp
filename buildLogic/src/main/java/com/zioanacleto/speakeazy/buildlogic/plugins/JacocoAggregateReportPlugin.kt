package com.zioanacleto.speakeazy.buildlogic.plugins

import com.zioanacleto.speakeazy.buildlogic.coverageExclusions
import com.zioanacleto.speakeazy.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoAggregateReportPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<JacocoReport>("jacocoAggregateReport") {
                group = "verification"
                description = "Generates aggregated JaCoCo coverage report"

                val jacocoVersion = libs.findVersion("jacoco").get().toString()

                jacocoClasspath = configurations.detachedConfiguration(
                    dependencies.create(
                        "org.jacoco:org.jacoco.ant:$jacocoVersion"
                    )
                )

                val execFiles = files(
                    subprojects.map { sub ->
                        sub.fileTree(sub.buildDir) {
                            include(
                                "jacoco/test*UnitTest.exec",
                                "outputs/unit_test_code_coverage/**/*.exec",
                                "outputs/code_coverage/**/*.ec" // androidTest for database module
                            )
                        }
                    }
                )

                executionData.setFrom(execFiles)

                val classDirs = files(
                    subprojects.map { sub ->
                        listOf(
                            "${sub.buildDir}/tmp/kotlin-classes/debug",
                            "${sub.buildDir}/intermediates/javac/debug/classes"
                        )
                    }.flatten()
                )

                classDirectories.setFrom(
                    classDirs.map {
                        fileTree(it) {
                            exclude(coverageExclusions)
                        }
                    }
                )

                sourceDirectories.setFrom(
                    files(
                        subprojects.map { it.projectDir.resolve("src/main/java") },
                        subprojects.map { it.projectDir.resolve("src/main/kotlin") }
                    )
                )

                dependsOn(
                    subprojects.flatMap { sub ->
                        sub.tasks.matching {
                            it.name == "testDebugUnitTest"
                        }
                    }
                )

                reports {
                    // setting html files' location
                    with(html) {
                        required.set(true)
                        outputLocation.set(layout.buildDirectory.dir("reports/jacoco/aggregate"))
                    }
                    // setting xml files' location
                    with(xml) {
                        required.set(true)
                        outputLocation.set(
                            layout.buildDirectory.file("reports/jacoco/jacocoAggregateReport.xml")
                        )
                    }
                }
            }
        }
    }
}