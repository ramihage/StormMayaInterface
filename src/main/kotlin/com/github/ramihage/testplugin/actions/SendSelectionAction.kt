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
        // Send the selected text to Maya
        val mayaOutput: String = DccInterface(4434).sendCodeToMaya(selectedText!!)

        val currentProject: Project = e.project ?: return
        val console = createOrGetConsole(currentProject)
        console.print(mayaOutput, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}