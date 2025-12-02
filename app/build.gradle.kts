import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("plugin.serialization").version(libs.versions.kotlin.serialization.get())
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.zioanacleto.featurefromtemplate.plugin)
    id("com.google.gms.google-services")
    alias(libs.plugins.devtools.ksp)
    jacoco
}

android {
    namespace = "com.zioanacleto.speakeazy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zioanacleto.speakeazy"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiToken = getLocalPropertiesVariable("apiToken")
        buildConfigField("String", "API_KEY", apiToken)

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.common)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Zioanacleto Buffa
    implementation(libs.zioanacleto.buffa)

    // Timber
    implementation(libs.timber)

    // Ktor
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client.logging)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // Lottie
    implementation(libs.lottie)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.gson)

    implementation(libs.kotlinx.serialization.json)
    implementation(kotlin("reflect"))

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.google.play.services.auth)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.ktor.client.mock)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val excludes = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/AndroidManifest.*",
        "**/*Activity.kt",
        "**/*Screen.kt",
        "**/di/**",
        "**/theme/**",
        "**/components/**",
        "**/navigation/**",
        "**/ApiClientImpl.**",
        "**/database/**"
    )

    val javaClasses = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(excludes)
    }

    val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(excludes)
    }

    classDirectories.setFrom(files(javaClasses, kotlinClasses))

    sourceDirectories.setFrom(files("$projectDir/src/main/java"))
    executionData.setFrom(
        file("$buildDir/jacoco/testDebugUnitTest.exec")
    )
}

private fun getLocalPropertiesVariable(variableName: String): String {
    val localProperties = rootProject.file("local.properties")
    return if (localProperties.exists()) {
        val properties = Properties()
        FileInputStream(localProperties).use {
            properties.load(it)
        }
        properties.getProperty(variableName)
    } else "\"testKey\""
}