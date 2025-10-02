package com.simonkaran.ai.kotlinmcpdebugger.plugin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpViewModel
import com.simonkaran.ai.kotlinmcpdebugger.ui.components.ConnectionConfigurationComponent
import com.simonkaran.ai.kotlinmcpdebugger.ui.components.ToolDetailsComponent
import com.simonkaran.ai.kotlinmcpdebugger.ui.components.ToolListComponent
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.ConnectionConfiguration
import com.simonkaran.ai.kotlinmcpdebugger.ui.theme.IntelliJMaterialTheme

@Composable
fun PluginWindowContent(project: Project, parentDisposable: Disposable) {
    val viewModel = remember { 
        McpViewModel().also { 
            Disposer.register(parentDisposable, it)
        } 
    }
    var cfg by remember { mutableStateOf(ConnectionConfiguration()) }

    IntelliJMaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                "MCP Inspector Lite",
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider()

            // Connection section
            ConnectionConfigurationComponent(
                connectionConfiguration = cfg,
                status = when (viewModel.connectionStatus) {
                    McpViewModel.ConnectionStatus.DISCONNECTED -> "Disconnected"
                    McpViewModel.ConnectionStatus.CONNECTING -> "Connecting..."
                    McpViewModel.ConnectionStatus.CONNECTED -> 
                        "Connected (${viewModel.availableTools.size} available tools)"
                    McpViewModel.ConnectionStatus.ERROR -> "Error"
                },
                onConnect = { viewModel.connect(cfg) },
                onDisconnect = { viewModel.disconnect() },
                onChange = { cfg = it }
            )

            // Error message display
            viewModel.errorMessage?.let { error ->
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Error:",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        val errorScrollState = rememberScrollState()
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .heightIn(max = 200.dp)
                                .verticalScroll(errorScrollState)
                        )
                    }
                }
            }

            HorizontalDivider()

            // Tools section
            if (viewModel.connectionStatus == McpViewModel.ConnectionStatus.CONNECTED) {
                ToolListComponent(
                    tools = viewModel.availableTools,
                    selectedTool = viewModel.selectedTool,
                    onToolSelect = { viewModel.selectTool(it) }
                )

                HorizontalDivider()

                // Tool details section
                ToolDetailsComponent(
                    selectedTool = viewModel.selectedTool,
                    requestParameters = viewModel.requestParameters,
                    resultJson = viewModel.resultJson,
                    onParametersChange = { viewModel.updateRequestParameters(it) },
                    onSend = { viewModel.sendRequest() },
                    onClear = { viewModel.clearRequest() }
                )
            }
        }
    }
}