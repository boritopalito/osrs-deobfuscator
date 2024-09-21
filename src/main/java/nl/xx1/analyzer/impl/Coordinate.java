package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Coordinate extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.superName.equals("java/lang/Object")
                && classNode.fields.size() == 3
                && fieldCount(
                                classNode,
                                f -> f.desc.equals("I") && Modifier.isPublic(f.access) && !Modifier.isStatic(f.access))
                        == 3;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<MethodNode> optional = classNode.methods.stream()
                .filter(m -> m.name.equals("<init>") && m.desc.equals("(III)V"))
                .findFirst();

        if (optional.isPresent()) {
            List<AbstractInsnNode> fieldNodes = Arrays.stream(
                            optional.get().instructions.toArray())
                    .toList()
                    .stream()
                    .filter(a -> a instanceof FieldInsnNode)
                    .toList();
            addField("plane", (FieldInsnNode) fieldNodes.get(0));
            addField("x", (FieldInsnNode) fieldNodes.get(1));
            addField("y", (FieldInsnNode) fieldNodes.get(2));
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
