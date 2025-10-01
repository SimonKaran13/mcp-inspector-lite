package com.simonkaran.ai.kotlinmcpdebugger.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.ConnectionConfiguration
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpTransportMode

@Composable
fun ConnectionConfigurationComponent(
    connectionConfiguration: ConnectionConfiguration,
    onChange: (ConnectionConfiguration) -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    status: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text("Connection", style = MaterialTheme.typography.titleMedium)

        // Transport selector
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Transport:", modifier = Modifier.padding(end = 8.dp))
            TransportSelect(connectionConfiguration.mode) { onChange(connectionConfiguration.copy(mode = it)) }
        }

        Spacer(Modifier.height(8.dp))
        when (connectionConfiguration.mode) {
            McpTransportMode.STDIO -> StdioFields(connectionConfiguration, onChange)
            McpTransportMode.STREAMABLE_HTTP -> HttpFields(connectionConfiguration, onChange)
        }

        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = onConnect) { Text("Connect") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onDisconnect) { Text("Disconnect") }
            Spacer(Modifier.width(16.dp))
            Text("Status: $status", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TransportSelect(
    selected: McpTransportMode,
    onSelect: (McpTransportMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = when (selected) {
                McpTransportMode.STDIO -> "STDIO"
                McpTransportMode.STREAMABLE_HTTP -> "HTTP"
            },
            onValueChange = {},
            readOnly = true,
            label = { Text("Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth()
        )
        ExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("STDIO") }, onClick = { onSelect(McpTransportMode.STDIO); expanded = false })
            DropdownMenuItem(text = { Text("HTTP") },  onClick = { onSelect(McpTransportMode.STREAMABLE_HTTP); expanded = false })
        }
    }
}

@Composable
private fun StdioFields(connectionConfiguration: ConnectionConfiguration, onChange: (ConnectionConfiguration) -> Unit) {
    OutlinedTextField(
        value = connectionConfiguration.command, onValueChange = { onChange(connectionConfiguration.copy(command = it)) },
        label = { Text("Command (e.g., uvx)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = connectionConfiguration.args, onValueChange = { onChange(connectionConfiguration.copy(args = it)) },
        label = { Text("Args (space-separated)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = connectionConfiguration.env, onValueChange = { onChange(connectionConfiguration.copy(env = it)) },
        label = { Text("Env (KEY=VAL;KEY2=VAL2)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun HttpFields(connectionConfiguration: ConnectionConfiguration, onChange: (ConnectionConfiguration) -> Unit) {
    OutlinedTextField(
        value = connectionConfiguration.url, onValueChange = { onChange(connectionConfiguration.copy(url = it)) },
        label = { Text("Base URL (e.g., https://host/_mcp)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = connectionConfiguration.headers, onValueChange = { onChange(connectionConfiguration.copy(headers = it)) },
        label = { Text("Headers (Key=Val;Key2=Val2)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
    )
}
