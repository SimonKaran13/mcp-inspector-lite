# MCP Inspector Lite

An IntelliJ IDEA plugin for inspecting and testing Model Context Protocol (MCP) servers.

## Quick Start

### 1. Running an MCP Server

This plugin is designed and tested to work with the **MCP Server Filesystem**. Start it with:

```bash
npx -y @modelcontextprotocol/server-filesystem /path/to/directory
```

For example, to inspect your home directory in MacOS:
```bash
npx -y @modelcontextprotocol/server-filesystem /Users/${USERNAME}
```

The server will provide tools like:
- `read_file` - Read file contents
- `write_file` - Write to files
- `list_directory` - List directory contents
- `search_files` - Search for files
- And more...

### 2. Building the Plugin

```bash
./gradlew build
```

The plugin JAR will be in `build/libs/kotlin-mcp-debugger-1.0-SNAPSHOT.jar`

### 3. Running the Plugin


```bash
./gradlew runIde
```

## Using the Plugin

1. In the tools bar in the left side of the IDE you should see the MCP Icon -> **MCP Inspector Lite**

2. Configure the connection:
   - **Transport:** `STDIO`
   - **Command:** `npx`
   - **Args:** `-y @modelcontextprotocol/server-filesystem /`
   - Leave **Env** empty

3. Click **Connect**

4. Browse available tools in the list

5. Select a tool, edit JSON parameters, and click **Send**

### Example: List Directory

1. Connect to the filesystem server
2. Select `list_directory` tool
3. Edit parameters:
   ```json
   {
     "path": "."
   }
   ```
4. Click **Send**
5. View results

### Dark Theme Support
The plugin supports dark theme, but you need to re-open the tool window so that the UI updates to the theme correctly.

## Requirements

- IntelliJ IDEA 2025.1.4.1 or later
- Java 21
- Node.js and npm/npx (for running MCP servers)

## AI use
See [AI Usage Documentation](docs/AI_PROMPTS.md)
