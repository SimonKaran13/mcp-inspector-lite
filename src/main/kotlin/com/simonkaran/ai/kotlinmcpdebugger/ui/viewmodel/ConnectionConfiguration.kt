package com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel

data class ConnectionConfiguration(
    val mode: McpTransportMode = McpTransportMode.STDIO,
    // stdio
    val command: String = "",
    val args: String = "",                 // space-separated
    val env: String = "",                  // e.g. KEY=VAL;KEY2=VAL2
    // http
    val url: String = "",
    val headers: String = ""               // e.g. Authorization=Bearer …;X-Id=123
)
