package com.simonkaran.ai.kotlinmcpdebugger.mcp

import com.simonkaran.ai.kotlinmcpdebugger.mcp.connection.McpServerConnectionDetails
import com.simonkaran.ai.kotlinmcpdebugger.mcp.connection.McpTransportMode
import io.ktor.client.HttpClient
import io.ktor.utils.io.streams.asInput
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.client.StreamableHttpClientTransport
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlin.time.Duration.Companion.seconds

class McpClient(
    private val serverDetails: McpServerConnectionDetails,
    private val httpClient: HttpClient,
    private val client: Client = Client(
        clientInfo = Implementation(
            name = "mcp-inspector-lite-client",
            version = "1.0.0"
        )
    )
) {
    private var isConnected = false

    suspend fun connect() = withContext(Dispatchers.IO) {
        client.connect(buildTransport())
        isConnected = true
    }

    suspend fun listTools(): List<McpTool> = withContext(Dispatchers.IO) {
        require(isConnected) { "No connection to the server, can't list tools" }

        client.listTools().tools.map { tool ->
            McpTool(
                name = tool.name,
                title = tool.title,
                description = tool.description,
                inputSchema = tool.inputSchema.properties
            )
        }
    }

    suspend fun callTool(name: String, args: Map<String, Any?>): String? = withContext(Dispatchers.IO) {
        require(isConnected) { "No connection to the server, can't call tool $name" }

        val response = client.callTool(name, args) ?: return@withContext null

        // Extract content from the response and convert to string
        response.content.joinToString("\n") { it.toString() }
    }

    private fun buildTransport(): Transport =
        when (serverDetails.transportMode) {
            McpTransportMode.STDIO -> buildStdioTransport()
            McpTransportMode.STREAMABLE_HTTP -> buildStreamableHttpTransport()
        }

    private fun buildStdioTransport(): StdioClientTransport {
        require(serverDetails.command != null) { "No command found for connecting to Stdio MCP server" }

        // Build the full command list: [command] + [args]
        val commandList = listOf(serverDetails.command) + serverDetails.args
        
        val process = ProcessBuilder(commandList)
            .redirectErrorStream(true)
            .apply {
                environment().putAll(serverDetails.env)
            }
            .start()
        
        return StdioClientTransport(
            input = process.inputStream.asInput().buffered(),
            output = process.outputStream.asSink().buffered()
        )
    }

    private fun buildStreamableHttpTransport(): StreamableHttpClientTransport {
        require(serverDetails.url != null) { "No URL found for connecting to Streamable HTTP MCP server" }

        return StreamableHttpClientTransport(
            client = httpClient,
            url = serverDetails.url,
            reconnectionTime = 5.seconds
        ) {
            serverDetails.headers.forEach { (k, v) ->
                headers.append(k, v)
            }
        }
    }
}