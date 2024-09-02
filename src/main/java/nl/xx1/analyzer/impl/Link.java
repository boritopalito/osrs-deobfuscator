package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.Analyzer;
import org.objectweb.asm.tree.ClassNode;

@Analyzer(
        name = "Link",
        description = "Node in a linked list",
        runAfter = {"Node"})
public class Link extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        if (!classNode.superName.equals("java/lang/Object")) {
            return false;
        }

        if (classNode.fields.size() != 2) return false;

        long selfCount = fieldCount(classNode, f -> f.desc.equals(String.format("L%s;", classNode.name)));

        if (selfCount != 2) return false;
        return true;
    }

    @Override
    public void matchFields(ClassNode classNode) {}

    @Override
    public void matchMethods(ClassNode classNode) {}
}
