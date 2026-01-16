package com.zioanacleto.speakeazy.buildlogic.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.zioanacleto.speakeazy.buildlogic.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class JacocoAndroidApplicationSettingsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jacoco")

            val androidExtension = extensions.getByType<ApplicationExtension>()

            androidExtension.buildTypes.configureEach {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }

            configureJacoco(extensions.getByType<ApplicationAndroidComponentsExtension>())
        }
    }
}