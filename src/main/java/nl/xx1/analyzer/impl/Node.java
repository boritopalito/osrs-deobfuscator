package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.Analyzer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Analyzer for identifying and analyzing the Node class in a data structure.
 *
 * <p>This analyzer is designed to identify a class that represents a Node in a linked data structure.
 * The Node class is expected to have the following characteristics:</p>
 *
 * <ul>
 *   <li>It does not extend any class (its superclass is java.lang.Object)</li>
 *   <li>It has exactly three fields:
 *     <ul>
 *       <li>One long field, representing a unique identifier (UID)</li>
 *       <li>Two fields of its own type, likely representing 'previous' and 'next' nodes</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This structure is typical for a node in a doubly-linked list or similar data structures.</p>
 */
@Analyzer(name = "Node", description = "This analyzer aims to identify the Node class.")
public class Node extends AbstractAnalyzer {

    /**
     * Determines if this analyzer can run on the given class.
     *
     * @param classNode The ClassNode to analyze.
     * @return true if the class matches the expected Node structure, false otherwise.
     */
    @Override
    public boolean canRun(ClassNode classNode) {
        // Check if the class directly extends Object
        if (!classNode.superName.equals("java/lang/Object")) return false;

        // Check if the class has exactly 3 fields
        if (classNode.fields.size() != 3) return false;

        // Count the number of long fields (expected to be 1 for the UID)
        long longCount = fieldCount(classNode, f -> f.desc.equals("J"));
        // Count the number of fields of the same type as the class itself (expected to be 2 for previous and next)
        long selfCount = fieldCount(classNode, f -> f.desc.equals(String.format("L%s;", classNode.name)));

        // The class is considered a Node if it has 1 long field and 2 fields of its own type
        return longCount == 1 && selfCount == 2;
    }

    /**
     * Identifies and adds the UID field to the analyzer's field map.
     *
     * @param classNode The ClassNode being analyzed.
     */
    @Override
    public void matchFields(ClassNode classNode) {
        for (FieldNode fieldNode : classNode.fields) {
            if (Type.getType(fieldNode.desc) == Type.LONG_TYPE) {
                addField("uid", fieldNode);
            }
            // Note: We don't add the 'previous' and 'next' fields here as they're not
            // distinguished. If needed, this could be extended to identify them based
            // on naming conventions or additional analysis.
        }
    }
}