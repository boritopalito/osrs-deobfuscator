package nl.xx1.analyzer.impl;

import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class DualNode extends AbstractAnalyzer implements Opcodes {
    @Override
    public boolean canRun(ClassNode classNode) {
        final String nodeName = context.getAnalyzer("Node").getClassNode().name;
        final long selfCount = fieldCount(classNode, f -> f.desc.equals(String.format("L%s;", classNode.name)));
        return classNode.superName.equals(nodeName) && selfCount == 2 && classNode.fields.size() == 2;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<MethodNode> optional =
                classNode.methods.stream().filter(m -> !m.name.equals("<init>")).findFirst();

        if (optional.isPresent()) {
            MethodNode method = optional.get();
            InstructionSearcher is = new InstructionSearcher(method.instructions, ALOAD, GETFIELD, IFNONNULL);

            if (is.match()) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) is.getFirstMatch().get(1);
                addMethod("unlink", method);
                addField("previous", fieldInsnNode);
            }
        }

        Optional<FieldNode> optionalFieldNode = classNode.fields.stream()
                .filter(f -> !f.name.equals(getField("previous").getObfuscatedName()))
                .findFirst();

        optionalFieldNode.ifPresent(fieldNode -> addField("next", fieldNode));
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
