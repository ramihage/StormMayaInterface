package com.github.ramihage.stormdccinterface.dccinterface

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.io.File
import java.net.Socket
import kotlin.io.path.absolutePathString

class DccInterface(private val port: Int) : AutoCloseable {
    private var client: Socket? = null

    companion object {
        private val _tempFile: File by lazy {
            File.createTempFile("maya_output", ".tmp").also {
                it.deleteOnExit() // Ensures the file is deleted when the JVM exits
            }
        }

        val tempFile: File
            get() = _tempFile
    }

    fun sendCodeToMaya(message: String): String {
        try {
            client = Socket("localhost", port)
            client?.use { socket ->
                // Set a timeout to prevent blocking indefinitely
                socket.soTimeout = 2000 // 1 second timeout

                val tempFilePath = _tempFile.toPath().absolutePathString()
                val fullCommand = buildString {
                    append("import maya.cmds as cmds\n")
                    append("import sys\n")
                    append("import traceback\n")
                    append("import os\n")  // For file cleanup
                    append("temp_file_path = r'%s'\n".format(tempFilePath))
                    append("cmds.cmdFileOutput(open=temp_file_path)\n")
                    append("try:\n")
                    append("    exec(%s)\n".format(Json.encodeToString(String.serializer(), message)))
                    append("except Exception as e:\n")
                    append("    with open(temp_file_path, 'a') as f:\n")
                    append("        f.write('Python Exception: ' + str(e) + '\\n')\n")
                    append("        f.write(traceback.format_exc() + '\\n')\n")
                    append("finally:\n")
                    append("    with open(temp_file_path, 'a') as f:\n")
                    append("        f.write('COMPLETED\\n')\n")
                    append("    cmds.cmdFileOutput(closeAll=True)\n")
                }
                socket.outputStream.write(fullCommand.toByteArray())
                socket.outputStream.flush()

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