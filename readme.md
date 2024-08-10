# OSRS Gamepack Deobfuscator and Hook Generator

## Overview

This project is a specialized framework designed to deobfuscate Old School RuneScape (OSRS) gamepacks and generate hooks for identifying classes, fields, and methods. It aims to provide researchers and developers with tools to understand and interact with the obfuscated OSRS client code.

**Note: This project is currently in active development.**

## Features

### Current Functionality

- **Analyzer System**: A flexible system for creating and managing analyzers for different aspects of the gamepack.
- **Node Class Analyzer**: Currently implemented to identify and analyze the obfuscated Node class within OSRS gamepacks.
- **Dynamic Analyzer Ordering**: Utilizes a topological sort to determine the execution order of analyzers based on their interdependencies.

### Planned Features

- **Enhanced ASM Node Wrappers**: Custom wrappers around ClassNode, MethodNode, and FieldNode for easier searching and analysis in obfuscated code.
- **Integer Multiplier Detection**: Tools to identify and analyze integer multipliers commonly used in obfuscated game logic.
- **Advanced Bytecode Analysis**: Sophisticated tools for searching and analyzing obfuscated Java bytecode.
- **Hook Generation**: Automated generation of hooks for identified classes, fields, and methods.

## Getting Started

(Instructions for setup and initial usage will be added as the project develops.)

## Usage

Currently, the framework includes a single analyzer for the Node class. Here's an example of how it's implemented:

```java
@Analyzer(name = "Node", description = "Identifies the obfuscated Node class.", runAfter = {}, runBefore = {"LinkedList", "DoublyLinkedList"})
public class NodeAnalyzer extends AbstractAnalyzer {
    @Override
    public boolean canAnalyze(ClassNode classNode) {
        // Implementation to identify the Node class in obfuscated code
    }

    @Override
    public void analyze(ClassNode classNode) {
        // Analysis and hook generation for the Node class
    }
}
```

## Development Roadmap

1. **ASM Node Wrapper Enhancements**:
    - Develop custom wrappers around ASM nodes for more efficient analysis of obfuscated code.
    - Estimated completion: [Date]

2. **Integer Multiplier Analysis**:
    - Create tools to detect and decode obfuscated integer multipliers in the gamepack.
    - Estimated completion: [Date]

3. **Advanced Bytecode Analysis Tools**:
    - Implement sophisticated methods for searching and analyzing obfuscated Java bytecode.
    - Estimated completion: [Date]

4. **Hook Generation System**:
    - Develop a system to automatically generate hooks for identified classes, fields, and methods.
    - Estimated completion: [Date]

5. **Additional Deobfuscation Analyzers**:
    - Implement analyzers for other key obfuscated structures in OSRS gamepacks.
    - Ongoing development

## Contributing

Contributions to the OSRS Gamepack Deobfuscator and Hook Generator are welcome! If you're interested in contributing, please [guidelines or contact information].

## License

[Include your chosen license here]

## Disclaimer

This tool is intended for educational and research purposes only. Users are responsible for ensuring their use of this tool complies with Jagex's terms of service for Old School RuneScape. The developers of this tool do not encourage or endorse any use that violates these terms.