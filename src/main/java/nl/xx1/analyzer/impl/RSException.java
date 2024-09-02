package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class RSException extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        if (!classNode.superName.equals("java/lang/RuntimeException")) {
            return false;
        }

        return fieldCount(classNode, f -> !Modifier.isStatic(f.access) && f.desc.equals("Ljava/lang/String;")) == 1;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (FieldNode fieldNode : classNode.fields) {
            if (getFields().size() == 2 || Modifier.isStatic(fieldNode.access)) continue;

            if (fieldNode.desc.equals("Ljava/lang/String;")) {
                addField("text", fieldNode);
            }

            if (fieldNode.desc.equals("Ljava/lang/Throwable;")) {
                addField("throwable", fieldNode);
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
