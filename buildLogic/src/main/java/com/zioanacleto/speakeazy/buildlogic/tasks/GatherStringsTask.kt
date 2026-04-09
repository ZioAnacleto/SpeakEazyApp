package com.zioanacleto.speakeazy.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.xml.parsers.DocumentBuilderFactory

abstract class GatherStringsTask : DefaultTask() {

    @get:InputFiles
    abstract val stringFiles: ConfigurableFileCollection

    @get:OutputFile
    abstract val outputJsonFile: RegularFileProperty

    @TaskAction
    fun gather() {
        val allStrings = mutableMapOf<String, String>()
        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()

        stringFiles.files.forEach { file ->
            if (file.exists()) {
                try {
                    val document = builder.parse(file)
                    document.documentElement.normalize()
                    val nodeList = document.getElementsByTagName("string")
                    for (i in 0 until nodeList.length) {
                        val node = nodeList.item(i)
                        val name = node.attributes.getNamedItem("name")?.nodeValue
                        val value = node.textContent
                        val isTranslatable = node.attributes.getNamedItem("translatable")?.nodeValue?.toBoolean() ?: true
                        if (name != null && isTranslatable) {
                            allStrings[name] = value
                        }
                    }
                } catch (e: Exception) {
                    println("Error parsing $file: ${e.message}")
                }
            }
        }

        val json = buildString {
            append("{\n  \"app\": \"speakeazy-android\",\n  \"strings\": [\n")
            val entries = allStrings.entries.toList()
            entries.forEachIndexed { index, entry ->
                append(
                    "    {\n      \"key\": \"${escape(entry.key)}\",\n      \"value\": \"${
                        escape(
                            entry.value
                        )
                    }\",\n      \"language\": \"en\"\n    }"
                )
                if (index < entries.size - 1) append(",")
                append("\n")
                if (index == entries.lastIndex) append("  ]\n")
            }
            append("}")
        }

        val outFile = outputJsonFile.get().asFile
        outFile.parentFile.mkdirs()
        outFile.writeText(json)

        println("Success: Gathered ${allStrings.size} strings into ${outFile.absolutePath}")
    }

    private fun escape(s: String): String {
        return s.replace("\\", "")
    }
}
