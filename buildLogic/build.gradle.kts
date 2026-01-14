plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `java-gradle-plugin`
    `kotlin-dsl`
}

group = "com.zioanacleto.speakeazy"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
}

gradlePlugin {
    plugins {
        register("coreConventionPlugin") {
            id = libs.plugins.zioanacleto.core.convention.plugin.get().pluginId
            implementationClass = "com.zioanacleto.speakeazy.buildlogic.plugins.CoreConventionPlugin"
        }
        register("jacocoAndroidLibrarySettingsPlugin") {
            id = libs.plugins.zioanacleto.jacoco.library.plugin.get().pluginId
            implementationClass = "com.zioanacleto.speakeazy.buildlogic.plugins.JacocoAndroidLibrarySettingsPlugin"
        }
        register("jacocoAndroidApplicationSettingsPlugin") {
            id = libs.plugins.zioanacleto.jacoco.application.plugin.get().pluginId
            implementationClass = "com.zioanacleto.speakeazy.buildlogic.plugins.JacocoAndroidApplicationSettingsPlugin"
        }
    }
}
