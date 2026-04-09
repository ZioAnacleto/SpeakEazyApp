package com.zioanacleto.speakeazy.buildlogic.plugins

import com.zioanacleto.speakeazy.buildlogic.resolveProperty
import com.zioanacleto.speakeazy.buildlogic.tasks.GatherStringsTask
import com.zioanacleto.speakeazy.buildlogic.tasks.UploadStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class StringsGathererPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project != project.rootProject) return

        val apiToken = project.resolveProperty("api.key")
        val backendUrl = project.resolveProperty("backend.url")

        val gatherTask = project.tasks.register(
            "gatherAllStrings",
            GatherStringsTask::class.java
        ) {
            group = "i18n"
            val resFiles = mutableListOf<File>()

            project.subprojects.forEach { subproject ->
                val resDir = File(subproject.projectDir, "src/main/res/values")
                if (resDir.exists()) {
                    resDir.listFiles()?.forEach { file ->
                        if (file.name == "strings.xml") {
                            resFiles.add(file)
                        }
                    }
                }
            }

            stringFiles.from(resFiles)
            outputJsonFile.set(project.layout.buildDirectory.file("outputs/strings/all_strings.json"))
        }

        project.tasks.register(
            "uploadStrings",
            UploadStringsTask::class.java
        ) {
            group = "i18n"
            dependsOn(gatherTask)

            inputJsonFile.set(
                project.layout.buildDirectory.file("outputs/strings/all_strings.json")
            )

            endpoint.set("$backendUrl/i18n/add")

            // Set the apiKey retrieved from local.properties
            apiKey.set(apiToken)
        }
    }
}
