package com.github.ramihage.testplugin.dccinterface

import java.io.IOException
import java.net.Socket

class DccInterface(private val port: Int) : AutoCloseable {
    private var client: Socket? = null

    fun sendCodeToMaya(message: String): String {
        try {
            client = Socket("localhost", port)
            client?.use { socket ->
                val outString = "python(\"%s\")".format(message)
                socket.outputStream.write(outString.toByteArray())
                // Read the response
                val inputStream = socket.inputStream
                val response = buildString {
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.available() > 0) {
                        bytesRead = inputStream.read(buffer)
                        if (bytesRead == -1) break
                        append(buffer.decodeToString(0, bytesRead))
                    }
                }
                return response
            }
        } catch (e: IOException) {
            return e.message ?: ""
        }
        // If the client is null, the connection was refused
        return ""
    }

    override fun close() {
        client?.close()
    }

}