package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.ConnectionConfiguration
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpTransportMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConnectionConfigurationTest {
    
    @Test
    fun `default configuration should use STDIO mode with empty values`() {
        val config = ConnectionConfiguration()
        
        assertEquals(McpTransportMode.STDIO, config.mode)
        assertEquals("", config.command)
        assertEquals("", config.args)
        assertEquals("", config.env)
    }
    
    @Test
    fun `should create valid STDIO configuration for filesystem server`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            command = "npx",
            args = "-y @modelcontextprotocol/server-filesystem /Users/test"
        )
        
        assertEquals(McpTransportMode.STDIO, config.mode)
        assertEquals("npx", config.command)
        assertTrue(config.args.contains("@modelcontextprotocol/server-filesystem"))
        assertTrue(config.args.contains("/Users/test"))
    }
    
    @Test
    fun `should handle environment variables with multiple key-value pairs`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            command = "python",
            env = "PATH=/usr/bin;DEBUG=true;API_KEY=secret123"
        )
        
        val envPairs = config.env.split(";")
        assertEquals(3, envPairs.size)
        assertTrue(envPairs.any { it.contains("PATH=") })
        assertTrue(envPairs.any { it.contains("DEBUG=") })
        assertTrue(envPairs.any { it.contains("API_KEY=") })
    }
    
    @Test
    fun `should create valid HTTP configuration with authorization header`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STREAMABLE_HTTP,
            url = "https://api.example.com/_mcp",
            headers = "Authorization=Bearer token123;Content-Type=application/json"
        )
        
        assertEquals(McpTransportMode.STREAMABLE_HTTP, config.mode)
        assertEquals("https://api.example.com/_mcp", config.url)
        assertTrue(config.headers.contains("Authorization=Bearer token123"))
    }
    
    @Test
    fun `should handle empty environment variables`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            command = "uvx",
            args = "mcp-server",
            env = ""
        )
        
        assertEquals("", config.env)
    }
    
    @Test
    fun `should preserve complex arguments with special characters`() {
        val config = ConnectionConfiguration(
            mode = McpTransportMode.STDIO,
            command = "node",
            args = "server.js --port=8080 --path=\"/some/path with spaces\""
        )
        
        assertTrue(config.args.contains("--port=8080"))
        assertTrue(config.args.contains("with spaces"))
    }
}
