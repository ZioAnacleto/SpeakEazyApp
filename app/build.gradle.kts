import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.FileInputStream
import java.io.StringReader
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory

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
    testImplementation(libs.kotlinx.coroutines.test)
}

// generating JaCoCo report for code coverage
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
        "**/Manifest*.*",
        "**/*Activity.*",
        "**/*Screen*.*",
        "**/di/**",
        "**/theme/**",
        "**/components/**",
        "**/navigation/**",
        "**/ApiClientImpl.kt",
        "**/database/**",
        "**/domain/**"
    )

    val classDirectoriesTree = fileTree("$buildDir") {
        include(
            "intermediates/javac/debug/classes/**",
            "tmp/kotlin-classes/debug/**"
        )
        exclude(excludes)
    }

    classDirectories.setFrom(classDirectoriesTree)

    sourceDirectories.setFrom(
        files(
            "$projectDir/src/main/java"
        )
    )

    executionData.setFrom(file("$buildDir/jacoco/testDebugUnitTest.exec"))
}

// calculates code coverage in percentage
// useful when interested in code coverage percentage locally
tasks.register("computeCoverage") {
    group = "verification"
    description = "Computes code coverage reading XML report from JaCoCo"

    val coverageType = "LINE" // in case we need to change it

    doLast {
        val reportFile = file("build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")

        require(reportFile.exists()) {
            "Report not found: $reportFile"
        }

        // Parser XML
        val factory = DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }

        val builder = factory.newDocumentBuilder().apply {
            setEntityResolver { _, _ ->
                InputSource(StringReader(""))
            }
        }

        val doc: Document = builder.parse(reportFile)

        // Estrae i counter globali (si trovano alla fine)
        val counters = doc.getElementsByTagName("counter")

        var missedLines = 0
        var coveredLines = 0

        for (i in 0 until counters.length) {
            val node = counters.item(i)
            val type = node.attributes.getNamedItem("type")?.nodeValue
            if (type == coverageType) {
                val covered = node.attributes.getNamedItem("covered").nodeValue.toInt()
                val missed = node.attributes.getNamedItem("missed").nodeValue.toInt()

                coveredLines = covered
                missedLines = missed
            }
        }

        val total = coveredLines + missedLines
        val coverage = if (total == 0) 0 else (100 * coveredLines / total)

        println("------ CODE COVERAGE ------")
        println("Lines covered: $coveredLines")
        println("Lines missed : $missedLines")
        println("Total lines  : $total")
        println("Coverage     : $coverage%")
        println("---------------------------")
    }
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