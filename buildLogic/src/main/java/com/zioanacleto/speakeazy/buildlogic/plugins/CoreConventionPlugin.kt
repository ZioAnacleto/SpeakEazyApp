package com.zioanacleto.speakeazy.buildlogic.plugins

import com.zioanacleto.speakeazy.buildlogic.CheckCoreModulesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CoreConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register(
                "checkCoreModules",
                CheckCoreModulesTask::class.java
            ) {
                group = "verification"
                description = "Verify that :core:* modules are aligned with CoreModule sealed class"
            }
        }
    }
}