package com.github.ramihage.stormdccinterface.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.ramihage.stormdccinterface.MyBundle
import javax.swing.*
import java.awt.Component.LEFT_ALIGNMENT
import javax.swing.BorderFactory
import java.awt.Dimension
import java.awt.BorderLayout

class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stormDccInterface = StormDccInterface()
        val content = ContentFactory.getInstance().createContent(stormDccInterface.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class StormDccInterface() {
        private val dccOptions = arrayOf("Maya", "Houdini", "Nuke")
        private val dccInfoMap = mapOf(
            "Maya" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandMaya"),
            "Houdini" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandHoudini"),
            "Nuke" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandNuke")
        )
        private val dccCloseInfoMap = mapOf(
            "Maya" to MyBundle.message("stormdccinterface.command.ClosePortCommandMaya"),
            "Houdini" to MyBundle.message("stormdccinterface.command.ClosePortCommandHoudini"),
            "Nuke" to MyBundle.message("stormdccinterface.command.ClosePortCommandNuke")
        )

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            
            // Create the combo box
            val dccSelector = JComboBox(dccOptions).apply {
                alignmentX = LEFT_ALIGNMENT
            }

            // Create a panel for the combo box
            val selectorPanel = JPanel(BorderLayout()).apply {  // Changed to BorderLayout
                add(JBLabel("Select DCC: "), BorderLayout.WEST)
                add(dccSelector, BorderLayout.CENTER)
                alignmentX = LEFT_ALIGNMENT
                maximumSize = Dimension(Integer.MAX_VALUE, preferredSize.height)
            }

            // Create dynamic label
            val labelText = MyBundle.message("stormdccinterface.command.Title", dccOptions[0])
            val dynamicLabel = JBLabel(labelText).apply {
                border = BorderFactory.createEmptyBorder(10, 0, 10, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            // Create the info text area
            val infoTextArea = JTextArea().apply {
                text = dccInfoMap[dccOptions[0]] ?: ""
                isEditable = false
                lineWrap = true
                wrapStyleWord = true
                background = UIManager.getColor("TextArea.background")
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                alignmentX = LEFT_ALIGNMENT
            }
            
            // Create scroll pane for text area
            val scrollPane = JScrollPane(infoTextArea).apply {
                border = BorderFactory.createLoweredBevelBorder()
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(Integer.MAX_VALUE, 100)
                maximumSize = Dimension(Integer.MAX_VALUE, 100)
                background = UIManager.getColor("TextArea.background")
                viewport.background = UIManager.getColor("TextArea.background")
                alignmentX = LEFT_ALIGNMENT
            }

            // Create port closing instructions widgets
            val closeLabelText = MyBundle.message("stormdccinterface.command.CloseTitle")
            val closeLabel = JBLabel(closeLabelText).apply {
                border = BorderFactory.createEmptyBorder(10, 0, 10, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            val closeInfoTextArea = JTextArea().apply {
                text = dccCloseInfoMap[dccOptions[0]] ?: ""
                isEditable = false
                lineWrap = true
                wrapStyleWord = true
                background = UIManager.getColor("TextArea.background")
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            // Create scroll pane for text area
            val closeScrollPane = JScrollPane(closeInfoTextArea).apply {
                border = BorderFactory.createLoweredBevelBorder()
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(Integer.MAX_VALUE, 100)
                maximumSize = Dimension(Integer.MAX_VALUE, 100)
                background = UIManager.getColor("TextArea.background")
                viewport.background = UIManager.getColor("TextArea.background")
                alignmentX = LEFT_ALIGNMENT
            }

            // Add action listener to combo box
            dccSelector.addActionListener {
                val selectedDcc = dccSelector.selectedItem as String
                infoTextArea.text = dccInfoMap.getOrDefault(selectedDcc, "")
                dynamicLabel.text = MyBundle.message("stormdccinterface.command.Title", selectedDcc)
                closeInfoTextArea.text = dccCloseInfoMap.getOrDefault(selectedDcc, "")
            }

            // Add components to the main panel
            add(Box.createRigidArea(Dimension(0, 5)))  // Add some spacing at the top
            add(selectorPanel)
            add(dynamicLabel)
            add(scrollPane)
            add(closeLabel)
            add(closeScrollPane)
            add(Box.createVerticalGlue())
        }
    }
}