package com.simonkaran.ai.kotlinmcpdebugger.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool

@Composable
fun ToolDetailsComponent(
    selectedTool: McpTool?,
    requestParameters: String,
    resultJson: String,
    onParametersChange: (String) -> Unit,
    onSend: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "Tool Details",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (selectedTool == null) {
            Text(
                "Select a tool from the list above to see details and invoke it.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(8.dp)
            )
        } else {
            // Tool info section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Name: ${selectedTool.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    selectedTool.description?.let {
                        Text(
                            "Description: $it",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Text(
                        "Params schema: ${selectedTool.inputSchema}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Request parameters section
            Text(
                "Send Request (JSON)",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            OutlinedTextField(
                value = requestParameters,
                onValueChange = onParametersChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = FontFamily.Monospace
                ),
                placeholder = { Text("{ }") }
            )

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onSend) {
                    Text("Send")
                }
                OutlinedButton(onClick = onClear) {
                    Text("Clear")
                }
            }

            // Result section
            Text(
                "Result",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    ),
                shape = MaterialTheme.shapes.small
            ) {
                val scrollState = rememberScrollState()
                Text(
                    text = resultJson.ifEmpty { "No result yet" },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (resultJson.isEmpty())
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(scrollState)
                )
            }
        }
    }
}

