package com.simonkaran.ai.kotlinmcpdebugger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool

@Composable
fun ToolListComponent(
    tools: List<McpTool>,
    selectedTool: McpTool?,
    onToolSelect: (McpTool) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "Tools",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (tools.isEmpty()) {
            Text(
                "No tools available. Connect to an MCP server to see tools.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(8.dp)
            )
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
        ) {
            items(tools) { tool ->
                ToolListItem(
                    tool = tool,
                    isSelected = tool == selectedTool,
                    onClick = { onToolSelect(tool) }
                )
                if (tool != tools.last()) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun ToolListItem(
    tool: McpTool,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tool.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface
            )
            tool.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Selection indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline,
                    CircleShape
                )
        )
    }
}

