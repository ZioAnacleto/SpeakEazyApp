package com.zioanacleto.speakeazy.buildlogic.plugins

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.zioanacleto.speakeazy.buildlogic.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class JacocoAndroidLibrarySettingsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jacoco")

            val androidExtension = extensions.getByType<LibraryExtension>()

            androidExtension.buildTypes.configureEach {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }

            configureJacoco(extensions.getByType<LibraryAndroidComponentsExtension>())
        }
    }
}