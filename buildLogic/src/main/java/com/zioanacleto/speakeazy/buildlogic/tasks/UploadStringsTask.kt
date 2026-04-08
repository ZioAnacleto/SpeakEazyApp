package com.zioanacleto.speakeazy.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
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

    @TaskAction
    fun upload() {
        val file = inputJsonFile.get().asFile

        if (!file.exists()) {
            throw GradleException("JSON file not found: ${file.absolutePath}")
        }

        val json = file.readText()

        val url = URI(endpoint.get()).toURL()
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")

            apiKey.orNull?.let {
                setRequestProperty("X-Api-Key", "$it")
            }

            connectTimeout = 30_000
            readTimeout = 180_000
        }

        connection.outputStream.use { os ->
            os.write(json.toByteArray())
        }

        val responseCode = connection.responseCode

        val response = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().readText()
        } else {
            connection.errorStream?.bufferedReader()?.readText()
        }

        println("Upload response code: $responseCode")
        println("Response body: $response")

        if (responseCode !in 200..299) {
            throw GradleException("Upload failed with code $responseCode")
        }

        val parentDir = file.parentFile
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                println("Successfully deleted temporary file: ${file.absolutePath}")
                
                // Also attempt to delete the parent "strings" folder if it's empty
                if (parentDir != null && parentDir.exists() && parentDir.isDirectory && parentDir.name == "strings") {
                    if (parentDir.delete()) {
                        println("Successfully deleted parent folder: ${parentDir.absolutePath}")
                    }
                }
            } else {
                println("Warning: Failed to delete temporary file: ${file.absolutePath}")
            }
        }
    }
}
