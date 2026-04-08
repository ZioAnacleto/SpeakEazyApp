package com.zioanacleto.speakeazy.buildlogic.plugins

import com.zioanacleto.speakeazy.buildlogic.getLocalProperty
import com.zioanacleto.speakeazy.buildlogic.tasks.GenerateI18nLibraryTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class I18nLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val apiToken = project.getLocalProperty("apiKey")
        val backendUrl = project.getLocalProperty("backend.url")

        project.tasks.register(
            "generateI18nLibrary",
            GenerateI18nLibraryTask::class.java
        ) {
            group = "i18n"
            description = "Fetches i18n strings from the backend and generates strings.xml files."
            
            // If applied to a subproject (like i18n-lib), use that project's directory
            outputDir.set(project.layout.projectDirectory)
            endpoint.set("$backendUrl/i18n/export")

            // Set the apiKey retrieved from local.properties
            apiKey.set(apiToken)
        }
    }
}
