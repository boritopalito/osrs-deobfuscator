package nl.xx1.analyzer.impl;

import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class NpcDefinition extends AbstractAnalyzer implements Opcodes {
    @Override
    public boolean canRun(ClassNode classNode) {
        String dualNodeName = context.getAnalyzer("DualNode").getClassNode().name;

        Optional<MethodNode> optional = classNode.methods.stream()
                .filter(m -> m.name.equals("<init>") && m.desc.equals("()V"))
                .findFirst();

        if (optional.isEmpty()) {
            return false;
        }

        InstructionSearcher is = new InstructionSearcher(
                optional.get().instructions, ALOAD, ICONST_5, ANEWARRAY, PUTFIELD, -1, -1, ALOAD);

        return classNode.superName.equals(dualNodeName)
                && classNode.fields.size() > 40
                && is.match()
                && ((FieldInsnNode) is.getFirstMatch().get(3)).desc.equals("[Ljava/lang/String;");
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (FieldNode field : classNode.fields) {
            if (field.desc.equals("Ljava/lang/String;")) {
                addField("name", field);
            }

            if (field.desc.equals("[Ljava/lang/String;")) {
                addField("actions", field);
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
