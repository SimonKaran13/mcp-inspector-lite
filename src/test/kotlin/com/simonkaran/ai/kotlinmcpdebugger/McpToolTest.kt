package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class McpToolTest {
    
    @Test
    fun `create tool with all fields`() {
        val schema = JsonObject(mapOf(
            "param1" to JsonPrimitive("string"),
            "param2" to JsonPrimitive("number")
        ))
        
        val tool = McpTool(
            name = "read_file",
            title = "Read File",
            description = "Reads a file from disk",
            inputSchema = schema
        )
        
        assertEquals("read_file", tool.name)
        assertEquals("Read File", tool.title)
        assertEquals("Reads a file from disk", tool.description)
        assertNotNull(tool.inputSchema)
    }
    
    @Test
    fun `tool with null optional fields`() {
        val tool = McpTool(
            name = "simple_tool",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        assertEquals("simple_tool", tool.name)
        assertEquals(null, tool.title)
        assertEquals(null, tool.description)
    }
}

