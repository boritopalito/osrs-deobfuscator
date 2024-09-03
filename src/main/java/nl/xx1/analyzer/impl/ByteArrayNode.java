package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class ByteArrayNode extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        String nodeName = context.getAnalyzer("Node").getClassNode().name;
        return classNode.superName.equals(nodeName)
                && classNode.fields.size() == 1
                && Modifier.isPublic(classNode.access)
                && fieldCount(classNode, f -> f.desc.equals("[B")) == 1;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<FieldNode> optional =
                classNode.fields.stream().filter(f -> f.desc.equals("[B")).findFirst();

        optional.ifPresent(fieldNode -> addField("bytes", fieldNode));
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
