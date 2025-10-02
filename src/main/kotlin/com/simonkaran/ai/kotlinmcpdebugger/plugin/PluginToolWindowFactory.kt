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
        val content = ContentFactory.getInstance().createContent(null, "", false)
        val root = JPanel(BorderLayout())
        root.add(
            ComposePanel().apply {
                setContent {
                    PluginWindowContent(project, content)
                }
             },
            BorderLayout.CENTER)
        content.component = root
        toolWindow.contentManager.addContent(content)
    }

}