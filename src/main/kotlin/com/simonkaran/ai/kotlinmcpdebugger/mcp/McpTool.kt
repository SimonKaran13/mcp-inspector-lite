package com.simonkaran.ai.kotlinmcpdebugger.mcp

import kotlinx.serialization.json.JsonObject

data class McpTool(
    val name: String,
    val title: String?,
    val description: String?,
    val inputSchema: JsonObject,
)
