# AI Assistant Usage Documentation

This document outlines how AI assistants were used during the development of MCP Inspector Lite.

## Tools Used

### ChatGPT
**Primary use:** Technical documentation lookup and API reference

**Why:** Faster and more contextual than searching using Google. Used as an intelligent search engine rather than a code generator.

**Approach:** Used ChatGPT to quickly understand concepts, then verified against official documentation. Never blindly copied code suggestions.

**Examples:**
- I'm not so familiar with kotlin, but I'm experienced in Java, so it helped me understand language features
- "What's the correct way to use IntelliJ's Disposable interface?"
- Looking up MCP SDK API methods and their signatures


### Cursor
**Primary use:** UI implementation and fast code iteration

**Why:** Excellent for rapidly iterating on UI components and fixing compilation errors (also I have a license from Uni and don't have a license for Jetbrains AI :P)

**Approach:** 
- Provided high-level requirements and let Cursor generate initial implementations
- Reviewed all generated code for correctness
- Made architectural decisions myself (e.g., state management approach, component structure)
- Debugged issues collaboratively - I identified problems, Cursor helped find solutions

**Key contributions:**
- Initial scaffolding of Compose UI components
- Debugging coroutine dispatcher conflicts
- Building the tool list and details components

## Decision-Making Process

### What I Did Myself
1. **Architecture decisions:**
   - Decided to use a ViewModel pattern for state management
   - Chose to separate UI components into logical modules
   - Determined how to structure the connection configuration

2. **MCP Client implementation**
    - Went over MCP Protocol documentation on https://modelcontextprotocol.io/
    - Coded the MCP client based on the documentation and the Kotlin MCP SDK

3. **Code review:**
   - Reviewed all AI-generated code before accepting
   - Refactored when suggestions didn't match project patterns
   - Simplified overly complex AI suggestions

### What AI Helped With
1. **Boilerplate reduction:**
   - Compose UI component structure
   - Data class definitions
   - Test file templates

2. **API discovery:**
   - Finding the right IntelliJ SDK APIs
   - Learning Kotlin
   - Understanding Compose

3. **Bug fixing:**
   - Coroutine dispatcher issues (used Dispatchers.IO + invokeLater pattern)
   - JSON parameter parsing (supporting multiple types)
   - Response content extraction (handling TextContent objects)
