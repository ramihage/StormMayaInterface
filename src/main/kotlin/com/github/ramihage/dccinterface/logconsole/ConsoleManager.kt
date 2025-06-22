package com.github.ramihage.testplugin.logconsole

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.project.Project

fun createOrGetConsole(project: Project): ConsoleView {
    // Try to find existing console
    val existingContent = RunContentManager.getInstance(project)
        .allDescriptors
        .find { it.displayName == "Dcc Output" }
        ?.executionConsole as? ConsoleView

    if (existingContent != null) {
        return existingContent
    }

    // Create new console if none exists
    val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

    // Create descriptor for the console
    val descriptor = RunContentDescriptor(
        consoleView,
        null,  // ProcessHandler is null since we're not running a process
        consoleView.component,
        "Dcc Output"
    )

    // Add the console to the Run tool window
    RunContentManager.getInstance(project).showRunContent(
        DefaultRunExecutor.getRunExecutorInstance(),
        descriptor
    )

    return consoleView
}