package com.zioanacleto.speakeazy.buildlogic

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project
import java.util.Properties

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

sealed class CoreModule(val name: String) {
    object Analytics: CoreModule(name = ":core:analytics")
    object Data: CoreModule(name = ":core:data")
    object Database: CoreModule(name = ":core:database")
    object Domain: CoreModule(name = ":core:domain")
    object Network: CoreModule(name = ":core:network")

    companion object {
        val all: List<CoreModule> by lazy {
            CoreModule::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .sortedBy { it.name }
        }
    }
}

fun DependencyHandler.coreModule(module: CoreModule) = project(module.name)
// to be used when testing new translated strings
fun DependencyHandler.i18nModule() = project(":i18n-lib")

fun Project.resolveProperty(
    key: String,
    envKey: String = key.uppercase().replace('.', '_')
): String {
    return providers.environmentVariable(envKey).orNull
        ?: providers.gradleProperty(key).orNull
        ?: getLocalProperty(key)
        ?: throw GradleException(
            "Missing property $key. " +
                    "Define it as ENV[$envKey], gradle.properties or local.properties"
        )
}

private fun Project.getLocalProperty(key: String): String? {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    return localProperties.getProperty(key)?.removeSurrounding("\"")
}