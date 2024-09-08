package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class AbstractDualNode extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        String dualNodeName = context.getAnalyzer("DualNode").getClassNode().name;
        return Modifier.isAbstract(classNode.access)
                && classNode.superName.equals(dualNodeName)
                && methodCount(classNode, methodNode -> Modifier.isAbstract(methodNode.access)) == 2;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (FieldNode field : classNode.fields) {
            if (field.desc.equals("I")) {
                addField("index", field);
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
