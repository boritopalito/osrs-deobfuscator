package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;

public class Producer extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return Modifier.isAbstract(classNode.access) && classNode.superName.equals("java/lang/Object");
    }

    @Override
    public void matchFields(ClassNode classNode) {}

    @Override
    public void matchMethods(ClassNode classNode) {}
}
