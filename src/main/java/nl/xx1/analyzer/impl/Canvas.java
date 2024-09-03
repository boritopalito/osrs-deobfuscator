package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class Canvas extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.superName.equalsIgnoreCase("java/awt/Canvas");
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<FieldNode> optional = classNode.fields.stream()
                .filter(f -> !Modifier.isStatic(f.access))
                .findFirst();

        optional.ifPresent(fieldNode -> addField("component", fieldNode));
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
