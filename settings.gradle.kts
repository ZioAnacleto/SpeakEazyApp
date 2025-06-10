import java.net.URI

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://zioanacleto-605134429054.d.codeartifact.eu-north-1.amazonaws.com/maven/zioanacleto_x_android/") {
            credentials {
                username = "aws"
                val processBuilder = ProcessBuilder(
                    "aws", "codeartifact", "get-authorization-token", "--domain", "zioanacleto",
                    "--domain-owner", "605134429054", "--region", "eu-north-1", "--query",
                    "authorizationToken", "--output", "text"
                )
                val process = processBuilder.start()
                val authorizationToken = process.inputStream.bufferedReader().readText()
                process.waitFor()

                if(process.exitValue() != 0)
                    throw GradleException("Failed to retrieve CodeArtifact token.")

                password = authorizationToken
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://zioanacleto-605134429054.d.codeartifact.eu-north-1.amazonaws.com/maven/zioanacleto_x_android/")
            credentials {
                username = "aws"
                val processBuilder = ProcessBuilder(
                    "aws", "codeartifact", "get-authorization-token", "--domain", "zioanacleto",
                    "--domain-owner", "605134429054", "--region", "eu-north-1", "--query",
                    "authorizationToken", "--output", "text"
                )
                val process = processBuilder.start()
                val authorizationToken = process.inputStream.bufferedReader().readText()
                process.waitFor()

                if(process.exitValue() != 0)
                    throw GradleException("Failed to retrieve CodeArtifact token.")

                password = authorizationToken
            }
        }
    }
}

rootProject.name = "SpeakEazy"
include(":app")
 