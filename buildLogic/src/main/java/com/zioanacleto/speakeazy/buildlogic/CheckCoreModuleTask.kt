package com.zioanacleto.speakeazy.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 *  This task would just check if core modules are correctly defined: when a new core module is
 *  created, it should be added to the [CoreModule] sealed class.
 */
abstract class CheckCoreModulesTask : DefaultTask() {

    @TaskAction
    fun check() {
        val declaredModules = CoreModule.all.map { it.name }.toSet()

        val actualCoreModules = project.rootProject
            .subprojects
            .map { it.path }
            .filter { it.startsWith(":core:") }
            .toSet()

        val missingInSealedClass = actualCoreModules - declaredModules
        val missingInProject = declaredModules - actualCoreModules

        if (missingInSealedClass.isNotEmpty() || missingInProject.isNotEmpty()) {
            val message = buildString {
                appendLine("❌ Core modules validation failed")

                if (missingInSealedClass.isNotEmpty()) {
                    appendLine("➜ Modules that are in the project but not defined in sealed class:")
                    missingInSealedClass.forEach { appendLine("   - $it") }
                }

                if (missingInProject.isNotEmpty()) {
                    appendLine("➜ Modules that are defined in sealed class but not in the project:")
                    missingInProject.forEach { appendLine("   - $it") }
                }
            }

            throw GradleException(message)
        }

        logger.lifecycle("✅ Core modules validation passed")
    }
}
