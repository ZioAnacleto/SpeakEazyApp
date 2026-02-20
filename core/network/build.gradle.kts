import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.zioanacleto.jacoco.library.plugin)
}

android {
    namespace = "com.zioanacleto.speakeazy.core.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val apiToken = getLocalPropertiesVariable("apiToken")
        buildConfigField("String", "API_KEY", apiToken)
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // Ktor
    implementation(libs.bundles.ktor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)
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