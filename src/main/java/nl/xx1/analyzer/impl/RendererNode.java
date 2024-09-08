package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;

public class RendererNode extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        String dualNodeName = context.getAnalyzer("DualNode").getClassNode().name;
        return Modifier.isAbstract(classNode.access)
                && classNode.superName.equals(dualNodeName)
                && fieldCount(classNode, f -> Modifier.isStatic(f.access)) > 0;
    }

    @Override
    public void matchFields(ClassNode classNode) {}

    @Override
    public void matchMethods(ClassNode classNode) {}
}
