package com.github.ramihage.testplugin.dccinterface

import com.github.ramihage.testplugin.logconsole.MyLogConsole
import com.github.ramihage.testplugin.notifications.DccNotifications
import com.intellij.notification.Notifications
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import java.io.IOException
import java.net.Socket

class DccInterface(private val port: Int) : AutoCloseable {
    private var client: Socket? = null

    fun sendCodeToMaya(message: String) {
        try {
            client = Socket("localhost", port)
            client?.use { socket ->
                val outString = "python(\"%s\")".format(message)
                socket.outputStream.write(outString.toByteArray())
                val dccResponse = socket.inputStream.read()
                // Handle response if needed
            }
        } catch (e: IOException) {
            Notifications.Bus.notify(DccNotifications.CONNECTION_REFUSED)
            throw e // Propagate the exception for proper handling
        }
    }

    override fun close() {
        client?.close()
    }
    
    fun printCodeToConsole(message: String?, project: Project?) {
        if (project == null) return
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("My Log Console") ?: return
        val content = toolWindow.contentManager.getContent(0) ?: return
        val console = content.component as MyLogConsole
        console.displayMessage("$message\n")
    }

}