package com.example.musicx.data.util

object FileUtils {

    private const val PATH_SEPARATOR = '/'


    const val EXTENSION_SEPARATOR = '.'

    const val HIDDEN_PATTERN = "/."

    fun name(path: String): String = path.substring(path.lastIndexOf(PATH_SEPARATOR) + 1)

    fun parent(path: String): String = path.replace("$PATH_SEPARATOR${name(path = path)}", "")

}





