package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool
import com.simonkaran.ai.kotlinmcpdebugger.ui.McpViewModel
import kotlinx.serialization.json.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonParameterParsingTest {
    
    @Test
    fun `should parse string parameters correctly`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        viewModel.updateRequestParameters("{\"path\": \"/home/user\", \"name\": \"test.txt\"}")
        
        assertTrue(viewModel.requestParameters.contains("path"))
        assertTrue(viewModel.requestParameters.contains("name"))
    }
    
    @Test
    fun `should handle numeric parameters`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        viewModel.updateRequestParameters("{\"count\": 42, \"ratio\": 3.14}")
        
        assertTrue(viewModel.requestParameters.contains("42"))
        assertTrue(viewModel.requestParameters.contains("3.14"))
    }
    
    @Test
    fun `should handle boolean parameters`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        viewModel.updateRequestParameters("{\"recursive\": true, \"verbose\": false}")
        
        assertTrue(viewModel.requestParameters.contains("true"))
        assertTrue(viewModel.requestParameters.contains("false"))
    }
    
    @Test
    fun `should handle mixed type parameters`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        val json = """
            {
                "path": "/home/user",
                "depth": 5,
                "recursive": true,
                "timeout": 30.5
            }
        """.trimIndent()
        
        viewModel.updateRequestParameters(json)
        
        assertTrue(viewModel.requestParameters.contains("path"))
        assertTrue(viewModel.requestParameters.contains("depth"))
        assertTrue(viewModel.requestParameters.contains("recursive"))
        assertTrue(viewModel.requestParameters.contains("timeout"))
    }
    
    @Test
    fun `should handle empty JSON object`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        viewModel.updateRequestParameters("{}")
        
        assertEquals("{}", viewModel.requestParameters)
    }
    
    @Test
    fun `should preserve JSON formatting`() {
        val viewModel = McpViewModel()
        val tool = McpTool(
            name = "test",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        val formattedJson = """
            {
              "key": "value",
              "number": 123
            }
        """.trimIndent()
        
        viewModel.updateRequestParameters(formattedJson)
        
        assertTrue(viewModel.requestParameters.contains("key"))
        assertTrue(viewModel.requestParameters.contains("value"))
        assertTrue(viewModel.requestParameters.contains("123"))
    }
}

