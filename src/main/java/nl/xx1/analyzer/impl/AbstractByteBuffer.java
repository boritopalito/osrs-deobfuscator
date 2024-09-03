package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;

public class AbstractByteBuffer extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return Modifier.isAbstract(classNode.access)
                && classNode.superName.equals("java/lang/Object")
                && classNode.fields.isEmpty()
                && methodCount(classNode, m -> m.desc.equals("(B)[B") && Modifier.isAbstract(m.access)) == 1
                && classNode.methods.size() > 1;
    }

    @Override
    public void matchFields(ClassNode classNode) {}

    @Override
    public void matchMethods(ClassNode classNode) {}
}
