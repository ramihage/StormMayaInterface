package com.github.ramihage.testplugin.actions

import com.github.ramihage.testplugin.MyBundle
import com.github.ramihage.testplugin.dccinterface.DccInterface
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.github.ramihage.testplugin.logconsole.createOrGetConsole
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project

class SendSelectionAction : DumbAwareAction(
    MyBundle.message("mayacharm.action.SendSelectionText"),
    MyBundle.message("mayacharm.action.SendSelectionDescription"),
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val selectionModel = e.getData(LangDataKeys.EDITOR)?.selectionModel ?: return
        val selectedText: String?

        if (selectionModel.hasSelection()) {
            selectedText = selectionModel.selectedText
        } else return

        val dccInterface = DccInterface(4434)
        // Send the selected text to Maya
        dccInterface.sendCodeToMaya(selectedText!!)
        
        // Give Maya a moment to write to the file
        Thread.sleep(100)
        
        val currentProject: Project = e.project ?: return
        val console = createOrGetConsole(currentProject)
        
        // Print header to the console
        console.print("Maya Output:\n", ConsoleViewContentType.SYSTEM_OUTPUT)
        
        // Read and process the temp file contents line by line
        try {
            DccInterface.tempFile.useLines { lines ->
                var isInErrorBlock = false
                lines.forEach { line ->
                    when {
                        line.startsWith("Python Exception: ") -> {
                            isInErrorBlock = true
                            console.print("$line\n", ConsoleViewContentType.ERROR_OUTPUT)
                        }
                        line == "COMPLETED" -> {
                            isInErrorBlock = false
                            console.print("$line\n", ConsoleViewContentType.NORMAL_OUTPUT)
                        }
                        isInErrorBlock -> {
                            console.print("$line\n", ConsoleViewContentType.ERROR_OUTPUT)
                        }
                        else -> {
                            console.print("$line\n", ConsoleViewContentType.NORMAL_OUTPUT)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            console.print("Error reading temp file: ${e.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
        }
    }
}