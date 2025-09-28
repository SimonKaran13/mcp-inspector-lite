package com.simonkaran.ai.kotlinmcpdebugger.mcp.connection

data class McpServerConnectionDetails (
    val transportMode: McpTransportMode,
    val command: String? = null,
    val args: List<String> = emptyList(),
    val env: Map<String, String> = emptyMap(),
    val url: String? = null,
    val headers: Map<String, String> = emptyMap(),
)