package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpViewModel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class McpViewModelTest {
    
    @Test
    fun `initial state should be disconnected with empty values`() {
        val viewModel = McpViewModel()
        
        assertEquals(McpViewModel.ConnectionStatus.DISCONNECTED, viewModel.connectionStatus)
        assertEquals(emptyList(), viewModel.availableTools)
        assertNull(viewModel.selectedTool)
        assertEquals("", viewModel.requestParameters)
        assertEquals("", viewModel.resultJson)
        assertNull(viewModel.errorMessage)
    }
    
    @Test
    fun `disconnect should clear all state including errors`() {
        val viewModel = McpViewModel()
        
        viewModel.updateRequestParameters("{\"test\": \"value\"}")
        
        viewModel.disconnect()
        
        assertEquals(McpViewModel.ConnectionStatus.DISCONNECTED, viewModel.connectionStatus)
        assertEquals(emptyList(), viewModel.availableTools)
        assertNull(viewModel.selectedTool)
        assertEquals("", viewModel.requestParameters)
        assertEquals("", viewModel.resultJson)
        assertNull(viewModel.errorMessage)
    }
    
    @Test
    fun `selecting a tool should generate request template from schema`() {
        val viewModel = McpViewModel()
        
        val schema = buildJsonObject {
            put("path", buildJsonObject {
                put("type", "string")
                put("description", "File path")
            })
            put("recursive", buildJsonObject {
                put("type", "boolean")
                put("description", "Recursive flag")
            })
        }
        
        val tool = McpTool(
            name = "list_directory",
            title = "List Directory",
            description = "Lists files in a directory",
            inputSchema = schema
        )
        
        viewModel.selectTool(tool)
        
        assertEquals(tool, viewModel.selectedTool)
        assertNotNull(viewModel.requestParameters)
        assertTrue(viewModel.requestParameters.contains("path"))
        assertTrue(viewModel.requestParameters.contains("recursive"))
    }
    
    @Test
    fun `selecting different tools should update parameters`() {
        val viewModel = McpViewModel()
        
        val tool1 = McpTool(
            name = "tool1",
            title = null,
            description = null,
            inputSchema = JsonObject(mapOf("param1" to JsonPrimitive("string")))
        )
        
        val tool2 = McpTool(
            name = "tool2",
            title = null,
            description = null,
            inputSchema = JsonObject(mapOf("param2" to JsonPrimitive("number")))
        )
        
        viewModel.selectTool(tool1)
        val params1 = viewModel.requestParameters
        
        viewModel.selectTool(tool2)
        val params2 = viewModel.requestParameters
        
        assertTrue(params1 != params2)
    }
    
    @Test
    fun `updating request parameters should preserve the value`() {
        val viewModel = McpViewModel()
        
        val json = "{\"path\": \"/Users/test\", \"recursive\": true}"
        viewModel.updateRequestParameters(json)
        
        assertEquals(json, viewModel.requestParameters)
    }
    
    @Test
    fun `clear request should regenerate template for selected tool`() {
        val viewModel = McpViewModel()
        
        val tool = McpTool(
            name = "test_tool",
            title = null,
            description = null,
            inputSchema = JsonObject(mapOf("param" to JsonPrimitive("string")))
        )
        
        viewModel.selectTool(tool)
        val originalTemplate = viewModel.requestParameters
        
        viewModel.updateRequestParameters("{\"custom\": \"value\"}")
        viewModel.clearRequest()
        
        assertEquals(originalTemplate, viewModel.requestParameters)
        assertEquals("", viewModel.resultJson)
        assertNull(viewModel.errorMessage)
    }
    
    @Test
    fun `selecting tool should clear previous results`() {
        val viewModel = McpViewModel()
        
        val tool = McpTool(
            name = "test_tool",
            title = null,
            description = null,
            inputSchema = JsonObject(emptyMap())
        )
        
        viewModel.selectTool(tool)
        
        assertEquals("", viewModel.resultJson)
    }
}
