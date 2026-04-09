package com.zioanacleto.speakeazy.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.HttpURLConnection
import java.net.URI

abstract class UploadStringsTask : DefaultTask() {

    @get:InputFile
    abstract val inputJsonFile: RegularFileProperty

    @get:Input
    abstract val endpoint: Property<String>

    @get:Optional
    @get:Input
    abstract val apiKey: Property<String>

    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 3000L
    }

    @TaskAction
    fun upload() {
        val file = inputJsonFile.get().asFile

        if (!file.exists()) {
            throw GradleException("JSON file not found: ${file.absolutePath}")
        }

        val json = file.readText()
        var lastError: String? = null

        repeat(MAX_ATTEMPTS) { attempt ->
            println("Upload attempt ${attempt + 1}/$MAX_ATTEMPTS")

            try {
                val connection = executeCall()

                connection.outputStream.use { os ->
                    os.write(json.toByteArray())
                }

                val responseCode = connection.responseCode

                val response = if (responseCode in 200..299) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream?.bufferedReader()?.readText()
                }

                println("Response code: $responseCode")
                println("Response body: $response")

                if (responseCode in 200..299) {
                    println("Upload successful ✅")

                    if (file.exists()) file.deleteFileAndParentFolder()
                    return
                } else {
                    lastError = "HTTP $responseCode: $response"
                }

            } catch (e: Exception) {
                lastError = e.message
                println("Upload failed with exception: ${e.message}")
            }

            if (attempt < MAX_ATTEMPTS - 1) {
                println("Retrying in ${RETRY_DELAY_MS}ms...")
                Thread.sleep(RETRY_DELAY_MS)
            }
        }

        throw GradleException("Upload failed after $MAX_ATTEMPTS attempts. Last error: $lastError")
    }

    private fun executeCall(): HttpURLConnection {
        val url = URI(endpoint.get()).toURL()
        return (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")

            apiKey.orNull?.let {
                setRequestProperty("X-Api-Key", "$it")
            }

            connectTimeout = 30_000
            readTimeout = 180_000
        }
    }

    private fun File.deleteFileAndParentFolder() {
        val deleted = delete()
        if (deleted) {
            println("Successfully deleted temporary file: $absolutePath")

            // Also attempt to delete the parent "strings" folder if it's empty
            if (parentFile.exists() && parentFile.isDirectory && parentFile.name == "strings") {
                if (parentFile.delete()) {
                    println("Successfully deleted parent folder: ${parentFile.absolutePath}")
                }
            }
        } else {
            println("Warning: Failed to delete temporary file: $absolutePath")
        }
    }
}
