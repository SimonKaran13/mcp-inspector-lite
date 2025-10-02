package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.ConnectionConfiguration
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpTransportMode
import kotlin.test.Test
import kotlin.test.assertEquals

class ConnectionConfigurationTest {
    
    @Test
    fun `test default configuration is STDIO mode`() {
        val config = ConnectionConfiguration()
        assertEquals(McpTransportMode.STDIO, config.mode)
    }
    
    @Test
    fun `test args parsing with multiple arguments`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            command = "npx",
            args = "-y @modelcontextprotocol/server-filesystem /path/to/dir"
        )
        
        assertEquals("npx", config.command)
        assertEquals("-y @modelcontextprotocol/server-filesystem /path/to/dir", config.args)
    }
    
    @Test
    fun `test environment variables format`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            env = "KEY1=value1;KEY2=value2"
        )
        
        assertEquals("KEY1=value1;KEY2=value2", config.env)
    }
    
    @Test
    fun `test HTTP configuration with headers`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STREAMABLE_HTTP,
            url = "https://example.com/_mcp",
            headers = "Authorization=Bearer token;X-Custom=value"
        )
        
        assertEquals(McpTransportMode.STREAMABLE_HTTP, config.mode)
        assertEquals("https://example.com/_mcp", config.url)
    }
}

