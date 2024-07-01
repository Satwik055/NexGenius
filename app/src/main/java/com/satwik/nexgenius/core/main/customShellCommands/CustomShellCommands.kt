package com.satwik.nexgenius.core.main.customShellCommands

import androidx.compose.ui.text.rememberTextMeasurer
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

fun getSystemInfo(): String {
    val os = System.getProperty("os.name")
    val osVersion = System.getProperty("os.version")
    val osArch = System.getProperty("os.arch")
    val user = System.getProperty("user.name")
    val javaVersion = System.getProperty("java.version")
    val runtimeMemory = Runtime.getRuntime().totalMemory()
    val freeMemory = Runtime.getRuntime().freeMemory()

    return """
        System Information:
        OS: $os
        Version: $osVersion
        Architecture: $osArch
        User: $user
        Java Version: $javaVersion
        Total Memory: $runtimeMemory
        Free Memory: $freeMemory
    """.trimIndent()
}

fun checkRoot(): String {
    val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )

    return if(
        paths.any { path -> File(path).exists()})
    {
        "Device is rooted"
    }
    else{
        "Device is not rooted"
    }
}

fun download(filename:String, socketOutput:OutputStream): String {
    TODO()
}



