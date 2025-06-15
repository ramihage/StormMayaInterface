package com.github.ramihage.testplugin.logconsole

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.execution.ui.ConsoleViewContentType.NORMAL_OUTPUT
import com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT

class MyLogConsole : ToolWindowFactory {

    private lateinit var consoleView: ConsoleView

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Create a console view using TextConsoleBuilderFactory
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(consoleView.component, "", false)
        contentManager.addContent(content)

        // Log sample messages
        consoleView.print("Hello, this is a log message!\n", NORMAL_OUTPUT)
        consoleView.print("This is an error message!\n", ERROR_OUTPUT)
    }
    fun displayMessage(message: String) {
        consoleView.print("$message\n", NORMAL_OUTPUT)
    }
}
