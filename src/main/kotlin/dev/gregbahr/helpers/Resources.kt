package dev.gregbahr.helpers

import java.io.File
import java.net.URI

object Resources {
    fun resourceAsString(fileName: String, delimiter: String = ""): String =
        resourceAsList(fileName).reduce { a, b -> "$a$delimiter$b" }

    fun resourceAsList(fileName: String): List<String> =
        File(fileName.toURI()).readLines()

    private fun String.toURI(): URI =
        Resources.javaClass.classLoader.getResource(this)?.toURI()
            ?: throw IllegalArgumentException("Cannot find Resource: $this")
}