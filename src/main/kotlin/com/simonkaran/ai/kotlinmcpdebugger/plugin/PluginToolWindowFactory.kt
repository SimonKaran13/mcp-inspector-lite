package com.simonkaran.ai.kotlinmcpdebugger.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import androidx.compose.ui.awt.ComposePanel
import javax.swing.JPanel
import java.awt.BorderLayout

class PluginToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val root = JPanel(BorderLayout())
        root.add(
            ComposePanel().apply {
                setContent {
                    PluginWindowContent(project)
                }
             },
            BorderLayout.CENTER)
        val content = ContentFactory.getInstance().createContent(root, "", false)
        toolWindow.contentManager.addContent(content)
    }

}