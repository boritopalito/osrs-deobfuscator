package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;

public class ByteBuffer extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        String nodeName = context.getAnalyzer("Node").getClassNode().name;

        return classNode.superName.equals(nodeName)
                && fieldCount(classNode, f -> f.desc.equals("[B")) == 1
                && fieldCount(classNode, f -> f.desc.equals("[J")) == 1;
    }

    @Override
    public void matchFields(ClassNode classNode) {}

    @Override
    public void matchMethods(ClassNode classNode) {}
}
