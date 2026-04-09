import java.time.LocalDate

// Local variable to be updated by the user for multiple releases on the same day
val dailyCounter = 1

fun computeNewVersion(): String {
    val now = LocalDate.now()
    return "${now.year}.${now.monthValue}.${now.dayOfMonth}.$dailyCounter"
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.zioanacleto.i18n.plugin) apply true
    alias(libs.plugins.zioanacleto.codeartifact.publishing.plugin) apply true
    alias(libs.plugins.zioanacleto.library.print.version.plugin) apply true
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("i18n-lib") {
            groupId = "com.zioanacleto.speakeazy"
            artifactId = "i18n-lib"
            version = computeNewVersion()
            
            artifact(layout.buildDirectory.file("outputs/aar/i18n-lib-release.aar"))
        }
    }
}

android {
    namespace = "com.zioanacleto.speakeazy.i18n_lib"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        version = computeNewVersion()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        setProperty("archivesBaseName", "i18n-lib-$version")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}