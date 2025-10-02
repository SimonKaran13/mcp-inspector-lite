package com.simonkaran.ai.kotlinmcpdebugger

import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool
import com.simonkaran.ai.kotlinmcpdebugger.ui.McpViewModel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class McpViewModelTest {
    
    @Test
    fun `initial state is disconnected`() {
        val viewModel = McpViewModel()
        assertEquals(McpViewModel.ConnectionStatus.DISCONNECTED, viewModel.connectionStatus)
    }
    
    @Test
    fun `disconnect clears all state`() {
        val viewModel = McpViewModel()
        
        // Set some state
        viewModel.updateRequestParameters("{\"test\": \"value\"}")
        
        // Disconnect
        viewModel.disconnect()
        
        assertEquals(McpViewModel.ConnectionStatus.DISCONNECTED, viewModel.connectionStatus)
        assertEquals(emptyList(), viewModel.availableTools)
        assertNull(viewModel.selectedTool)
        assertEquals("", viewModel.requestParameters)
    }
    
    @Test
    fun `selecting a tool updates the state`() {
        val viewModel = McpViewModel()
        
        val tool = McpTool(
            name = "test_tool",
            title = "Test Tool",
            description = "A test tool",
            inputSchema = JsonObject(mapOf("param1" to JsonPrimitive("string")))
        )
        
        viewModel.selectTool(tool)
        
        assertEquals(tool, viewModel.selectedTool)
        assertNotNull(viewModel.requestParameters)
    }
    
    @Test
    fun `updating request parameters works`() {
        val viewModel = McpViewModel()
        
        val json = "{\"key\": \"value\"}"
        viewModel.updateRequestParameters(json)
        
        assertEquals(json, viewModel.requestParameters)
    }
    
    @Test
    fun `clear request resets parameters and result`() {
        val viewModel = McpViewModel()
        
        viewModel.updateRequestParameters("{\"test\": \"value\"}")
        viewModel.clearRequest()
        
        assertEquals("", viewModel.requestParameters)
        assertEquals("", viewModel.resultJson)
    }
}

