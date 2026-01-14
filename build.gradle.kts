// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    `kotlin-dsl`
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
}

tasks.register<JacocoReport>("jacocoAggregateReport") {
    group = "verification"
    description = "Generates aggregated JaCoCo coverage report"

    jacocoClasspath = configurations.detachedConfiguration(
        dependencies.create("org.jacoco:org.jacoco.ant:0.8.12")
    )

    val execFiles = files(
        subprojects.map { sub ->
            sub.fileTree(sub.buildDir) {
                include(
                    "jacoco/test*UnitTest.exec",
                    "outputs/unit_test_code_coverage/**/*.exec"
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

    val coverageExclusions = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Activity.*",
        "**/*Screen*.*",
        "**/di/**",
        "**/theme/**",
        "**/components/**",
        "**/navigation/**"
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
        html.required = true
        xml.required = true
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/aggregate"))
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/jacocoAggregateReport.xml")
        )
    }
}