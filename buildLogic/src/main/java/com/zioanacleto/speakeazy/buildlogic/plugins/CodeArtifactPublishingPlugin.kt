package com.zioanacleto.speakeazy.buildlogic.plugins

import com.zioanacleto.speakeazy.buildlogic.resolveProperty
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import java.net.URI

class CodeArtifactPublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.withId("maven-publish") {
                configureExtension {
                    repositories {
                        setupMaven {
                            val domain = resolveProperty(key = PROPERTY_MAVEN_DOMAIN)
                            val owner = resolveProperty(key = PROPERTY_MAVEN_OWNER)
                            val mavenUrl = resolveProperty(key = PROPERTY_MAVEN_URL)

                            url = URI(mavenUrl)
                            credentials {
                                username = CODE_ARTIFACT_USER
                                password = retrieveAuthorizationCode(domain, owner)
                            }
                        }
                    }
                }
            }
        }
    }

    // code to retrieve AWS CodeArtifact refreshed authorization token
    private fun retrieveAuthorizationCode(
        domain: String,
        owner: String
    ): String {
        val processBuilder = ProcessBuilder(
            "aws", "codeartifact", "get-authorization-token", "--domain", domain,
            "--domain-owner", owner, "--region", "eu-north-1", "--query",
            "authorizationToken", "--output", "text"
        )
        val process = processBuilder.start()
        val authorizationToken = process.inputStream.bufferedReader().readText()
        process.waitFor()

        if (process.exitValue() != 0)
            throw GradleException("Failed to retrieve CodeArtifact token.")

        return authorizationToken
    }

    private fun Project.configureExtension(
        configure: PublishingExtension.() -> Unit
    ) = extensions.configure(PublishingExtension::class.java) { configure() }

    private fun RepositoryHandler.setupMaven(
        setup: MavenArtifactRepository.() -> Unit
    ) = maven { setup() }

    companion object {
        private const val CODE_ARTIFACT_USER = "aws"
        private const val PROPERTY_MAVEN_DOMAIN = "maven.domain"
        private const val PROPERTY_MAVEN_OWNER = "maven.owner"
        private const val PROPERTY_MAVEN_URL = "maven.url"
    }
}
