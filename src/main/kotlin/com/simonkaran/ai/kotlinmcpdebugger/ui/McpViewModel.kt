package com.simonkaran.ai.kotlinmcpdebugger.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpClient
import com.simonkaran.ai.kotlinmcpdebugger.mcp.McpTool
import com.simonkaran.ai.kotlinmcpdebugger.mcp.connection.McpServerConnectionDetails
import com.simonkaran.ai.kotlinmcpdebugger.mcp.connection.McpTransportMode
import com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.ConnectionConfiguration
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

class McpViewModel : Disposable {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val httpClient = HttpClient(CIO)
    private var mcpClient: McpClient? = null

    var connectionStatus by mutableStateOf(ConnectionStatus.DISCONNECTED)
        private set

    var availableTools by mutableStateOf<List<McpTool>>(emptyList())
        private set

    var selectedTool by mutableStateOf<McpTool?>(null)
        private set

    var requestParameters by mutableStateOf("")
        private set

    var resultJson by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun connect(config: ConnectionConfiguration) {
        scope.launch {
            try {
                invokeLater {
                    connectionStatus = ConnectionStatus.CONNECTING
                    errorMessage = null
                }

                val serverDetails = config.toServerConnectionDetails()
                mcpClient = McpClient(serverDetails, httpClient)
                mcpClient?.connect()

                // Load tools after connection
                loadTools()
                
                invokeLater {
                    connectionStatus = ConnectionStatus.CONNECTED
                }
            } catch (e: Exception) {
                invokeLater {
                    connectionStatus = ConnectionStatus.ERROR
                    errorMessage = "Connection failed: ${e.javaClass.simpleName}: ${e.message}"
                }
                mcpClient = null
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        mcpClient = null
        availableTools = emptyList()
        selectedTool = null
        requestParameters = ""
        resultJson = ""
        errorMessage = null
        connectionStatus = ConnectionStatus.DISCONNECTED
    }

    fun selectTool(tool: McpTool) {
        selectedTool = tool
        // Initialize request parameters with schema template
        requestParameters = buildJsonTemplate(tool.inputSchema)
        resultJson = ""
    }

    fun updateRequestParameters(json: String) {
        requestParameters = json
    }

    fun clearRequest() {
        requestParameters = selectedTool?.let { buildJsonTemplate(it.inputSchema) } ?: ""
        resultJson = ""
        errorMessage = null
    }

    fun sendRequest() {
        val tool = selectedTool ?: return
        scope.launch {
            try {
                invokeLater {
                    errorMessage = null
                }
                val params = parseJsonParameters(requestParameters)
                val result = mcpClient?.callTool(tool.name, params)
                invokeLater {
                    resultJson = result?.let { formatJson(it) } ?: "{}"
                }
            } catch (e: Exception) {
                invokeLater {
                    errorMessage = "Request failed: ${e.javaClass.simpleName}: ${e.message}"
                    resultJson = ""
                }
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadTools() {
        try {
            val tools = mcpClient?.listTools() ?: emptyList()
            invokeLater {
                availableTools = tools
            }
        } catch (e: Exception) {
            invokeLater {
                errorMessage = "Failed to load tools: ${e.javaClass.simpleName}: ${e.message}"
                availableTools = emptyList()
            }
            e.printStackTrace()
        }
    }
    
    private fun invokeLater(block: () -> Unit) {
        ApplicationManager.getApplication().invokeLater(block)
    }

    private fun parseJsonParameters(json: String): Map<String, Any?> {
        return try {
            val jsonElement = Json.parseToJsonElement(json)
            if (jsonElement is JsonObject) {
                jsonElement.mapValues { (_, value) ->
                    when (value) {
                        is kotlinx.serialization.json.JsonPrimitive -> {
                            when {
                                value.isString -> value.content
                                value.content == "true" || value.content == "false" -> value.content.toBoolean()
                                value.content.toIntOrNull() != null -> value.content.toInt()
                                value.content.toDoubleOrNull() != null -> value.content.toDouble()
                                else -> value.content
                            }
                        }
                        is JsonObject -> value.toString()
                        is kotlinx.serialization.json.JsonArray -> value.toString()
                        else -> value.toString()
                    }
                }
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    private fun buildJsonTemplate(schema: JsonObject): String {
        return Json { prettyPrint = true }.encodeToString(
            JsonObject.serializer(),
            schema.mapValues { (_, value) ->
                when {
                    value is JsonObject && value["type"]?.toString()?.contains("string") == true -> JsonPrimitive("")
                    value is JsonObject && value["type"]?.toString()?.contains("number") == true -> JsonPrimitive(0)
                    value is JsonObject && value["type"]?.toString()?.contains("boolean") == true -> JsonPrimitive(false)
                    else -> JsonPrimitive("")
                }
            }.let { JsonObject(it) }
        )
    }

    private fun formatJson(json: String): String {
        return try {
            val element = Json.parseToJsonElement(json)
            Json { prettyPrint = true }.encodeToString(JsonElement.serializer(), element)
        } catch (e: Exception) {
            json
        }
    }

    override fun dispose() {
        scope.cancel()
        httpClient.close()
    }

    enum class ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }
}

private fun ConnectionConfiguration.toServerConnectionDetails(): McpServerConnectionDetails {
    return when (mode) {
        com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpTransportMode.STDIO -> {
            val argsList = args.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
            val envMap = if (env.isNotBlank()) {
                env.split(";")
                    .filter { it.contains("=") }
                    .associate {
                        val (k, v) = it.split("=", limit = 2)
                        k.trim() to v.trim()
                    }
            } else {
                emptyMap()
            }
            McpServerConnectionDetails(
                transportMode = McpTransportMode.STDIO,
                command = command,
                args = argsList,
                env = envMap
            )
        }
        com.simonkaran.ai.kotlinmcpdebugger.ui.viewmodel.McpTransportMode.STREAMABLE_HTTP -> {
            val headersMap = if (headers.isNotBlank()) {
                headers.split(";")
                    .filter { it.contains("=") }
                    .associate {
                        val (k, v) = it.split("=", limit = 2)
                        k.trim() to v.trim()
                    }
            } else {
                emptyMap()
            }
            McpServerConnectionDetails(
                transportMode = McpTransportMode.STREAMABLE_HTTP,
                url = url,
                headers = headersMap
            )
        }
    }
}