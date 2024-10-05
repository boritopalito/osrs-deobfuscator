# OSRS Game Deobfuscator and Updater

## Overview

This project is a sophisticated Java-based tool designed for deobfuscating, analyzing, and understanding compiled Java game code, with a specific focus on Old School RuneScape (OSRS). It provides a suite of utilities for reverse engineering, including class analysis, method and field identification, multiplier detection, and hook generation.

## Key Features

1. **Deobfuscation**: Removes unused methods and fields from obfuscated code.
2. **Class Analysis**: Identifies and analyzes key game classes such as Node, Link, LinkedList, etc.
3. **Multiplier Detection**: Finds and catalogs arithmetic multipliers used in the game code.
4. **Hook Generation**: Creates JSON hooks for identified classes, methods, and fields.
5. **ASM Integration**: Utilizes the ASM library for bytecode manipulation and analysis.
6. **Extensible Architecture**: Allows easy addition of new analyzers and deobfuscation techniques.

## Project Structure

- `src/main/java/nl/xx1/`:
   - `analyzer/`: Contains the core analyzer framework and implementations.
   - `deobfuscation/`: Houses deobfuscation algorithms and utilities.
   - `hooks/`: Classes for generating and managing hooks.
   - `utilities/`: Various utility classes for tasks like JAR manipulation and instruction searching.

## Key Components

### Analyzers
- Located in `nl.xx1.analyzer.impl`
- Each analyzer (e.g., `Node`, `Client`, `LinkedList`) is responsible for identifying and analyzing specific game classes.

### Deobfuscators
- Found in `nl.xx1.deobfuscation.impl`
- Includes `UnusedMethods` and `UnusedFields` for removing redundant code.

### Utilities
- `JarUtilities`: Handles JAR file operations.
- `MultiplierFinder`: Detects arithmetic multipliers in the code.
- `InstructionSearcher`: Facilitates bytecode instruction pattern matching.

### Hook Generation
- `Hooks` class generates JSON output of identified game structures.

## Usage

1. Place the target JAR file (e.g., OSRS game client) in the `gamepacks/` directory.
2. Run the `Updater` class, specifying the path to the JAR file.
3. The tool will:
   - Deobfuscate the code
   - Analyze and identify key classes and structures
   - Generate hooks in the `hooks/` directory
   - Save deobfuscated classes in the `deob-gamepacks/` directory

## Requirements

- Java JDK 11 or higher
- Maven for dependency management
- ASM library for bytecode manipulation
- Jackson library for JSON processing

## Building and Running

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/java-game-deobfuscator.git
   ```
2. Navigate to the project directory:
   ```
   cd java-game-deobfuscator
   ```
3. Build the project with Maven:
   ```
   mvn clean install
   ```
4. Run the `Updater` class:
   ```
   java -cp target/classes nl.xx1.Updater
   ```

## Extending the Project

- To add new analyzers, create a new class in `nl.xx1.analyzer.impl` extending `AbstractAnalyzer`.
- Implement new deobfuscation techniques by adding classes to `nl.xx1.deobfuscation.impl`.

## Caution

This tool is designed for educational and research purposes. Ensure you have the right to analyze and deobfuscate any code you use it on.

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests with any enhancements, bug fixes, or improvements.

## License

[Specify your license here, e.g., MIT, GPL, etc.]

## Disclaimer

This project is not affiliated with or endorsed by Jagex or Old School RuneScape. Use responsibly and at your own risk.