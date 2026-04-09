package com.zioanacleto.speakeazy.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class GenerateI18nLibraryTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract val endpoint: Property<String>

    @get:Optional
    @get:Input
    abstract val apiKey: Property<String>

    @TaskAction
    fun generate() {
        // API call to BE
        val url = URI(endpoint.get()).toURL()
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json")

            apiKey.orNull?.let {
                setRequestProperty("X-Api-Key", it)
            }

            connectTimeout = 15_000
            readTimeout = 30_000
        }

        val responseCode = connection.responseCode
        if (responseCode !in 200..299) {
            val errorBody = connection.errorStream?.bufferedReader()?.readText()
            throw GradleException("Failed to fetch i18n data: $responseCode - $errorBody")
        }

        val json = connection.inputStream.bufferedReader().use { it.readText() }
        
        val parsedData = parseJson(json)

        parsedData.forEach { (language, translations) ->
            val folderName = if (language == "en") "values" else "values-$language"
            val dir = File(outputDir.get().asFile, "src/main/res/$folderName")
            dir.mkdirs()

            val file = File(dir, "strings.xml")
            writeXmlFile(file, translations)
            println("Generated strings.xml for language: $language")
        }
    }

    private fun writeXmlFile(file: File, translations: Map<String, String>) {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.newDocument()

        val resourcesElement = doc.createElement("resources")
        doc.appendChild(resourcesElement)

        translations.forEach { (key, value) ->
            val stringElement = doc.createElement("string")
            stringElement.setAttribute("name", key)
            // Escape for Android: ' becomes \' and " becomes \"
            // Also ensure newlines are handled as \n
            val escapedValue = value
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
            
            stringElement.textContent = escapedValue
            resourcesElement.appendChild(stringElement)
        }

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        
        val source = DOMSource(doc)
        val result = StreamResult(file)
        transformer.transform(source, result)
    }

    private fun parseJson(json: String): Map<String, Map<String, String>> {
        val result = mutableMapOf<String, Map<String, String>>()
        
        // Find the "languages" array
        val languagesStartIndex = json.indexOf("\"languages\"")
        if (languagesStartIndex == -1) return result
        
        val arrayStart = json.indexOf("[", languagesStartIndex)
        if (arrayStart == -1) return result
        val arrayEnd = findClosingBracket(json, arrayStart, '[', ']')
        
        val languagesArrayContent = json.substring(arrayStart + 1, arrayEnd)
        val languageBlocks = splitJsonObjects(languagesArrayContent)

        languageBlocks.forEach { block ->
            val lang = extractValue(block, "language")
            val stringsStart = block.indexOf("\"strings\"")
            if (stringsStart != -1) {
                val stringsArrayStart = block.indexOf("[", stringsStart)
                if (stringsArrayStart != -1) {
                    val stringsArrayEnd = findClosingBracket(block, stringsArrayStart, '[', ']')
                    
                    val stringsArrayContent = block.substring(stringsArrayStart + 1, stringsArrayEnd)
                    val stringEntries = splitJsonObjects(stringsArrayContent)
                    
                    val translations = mutableMapOf<String, String>()
                    stringEntries.forEach { entry ->
                        val key = extractValue(entry, "key")
                        val value = extractValue(entry, "value")
                        if (key.isNotEmpty()) {
                            translations[key] = value
                        }
                    }
                    if (lang.isNotEmpty()) {
                        result[lang] = translations
                    }
                }
            }
        }
        
        return result
    }

    private fun extractValue(json: String, key: String): String {
        val keyWithQuotes = "\"$key\""
        val keyIndex = json.indexOf(keyWithQuotes)
        if (keyIndex == -1) return ""
        
        val colonIndex = json.indexOf(":", keyIndex)
        val quoteStart = json.indexOf("\"", colonIndex)
        if (quoteStart == -1) return ""
        
        val sb = StringBuilder()
        var escaped = false
        for (i in (quoteStart + 1) until json.length) {
            val char = json[i]
            if (escaped) {
                when (char) {
                    '\"' -> sb.append('\"')
                    'n' -> sb.append('\n')
                    'r' -> sb.append('\r')
                    't' -> sb.append('\t')
                    '\\' -> sb.append('\\')
                    '/' -> sb.append('/')
                    else -> sb.append('\\').append(char)
                }
                escaped = false
            } else if (char == '\\') {
                escaped = true
            } else if (char == '\"') {
                break
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }

    private fun findClosingBracket(text: String, start: Int, open: Char, close: Char): Int {
        var count = 0
        for (i in start until text.length) {
            if (text[i] == open) count++
            else if (text[i] == close) {
                count--
                if (count == 0) return i
            }
        }
        return text.length
    }

    private fun splitJsonObjects(json: String): List<String> {
        val result = mutableListOf<String>()
        var bracketCount = 0
        var current = StringBuilder()
        var inQuotes = false
        var escaped = false

        for (char in json) {
            if (char == '\"' && !escaped) inQuotes = !inQuotes
            escaped = char == '\\' && !escaped
            
            if (!inQuotes) {
                if (char == '{') bracketCount++
                if (char == '}') {
                    bracketCount--
                    if (bracketCount == 0) {
                        current.append(char)
                        result.add(current.toString().trim())
                        current = StringBuilder()
                        continue
                    }
                }
            }
            
            if (bracketCount > 0 || inQuotes) {
                current.append(char)
            }
        }
        return result
    }
}
