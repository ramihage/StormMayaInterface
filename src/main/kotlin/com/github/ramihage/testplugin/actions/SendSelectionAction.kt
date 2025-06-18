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
        
        // Read the temp file contents
        val tempFileContents = try {
            DccInterface.tempFile.readText()
        } catch (e: Exception) {
            "Error reading temp file: ${e.message}"
        }

        val currentProject: Project = e.project ?: return
        val console = createOrGetConsole(currentProject)
        
        // Print the temp file contents to the console
        console.print("Maya Output:\n", ConsoleViewContentType.SYSTEM_OUTPUT)
        console.print("$tempFileContents\n", ConsoleViewContentType.NORMAL_OUTPUT)
    }
}