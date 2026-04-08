package com.zioanacleto.speakeazy.buildlogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class PrintAndroidLibraryVersion: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register("printModuleVersion") {
                doLast {
                    logger.lifecycle("${target.version}")
                }
            }
        }
    }
}