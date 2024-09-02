package nl.xx1.analyzer.impl;

import java.util.List;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.tree.*;

public class ItemStorage extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        String nodeName = context.getAnalyzer("Node").getClassNode().name;

        Optional<MethodNode> optional =
                classNode.methods.stream().filter(m -> m.name.equals("<init>")).findFirst();

        return classNode.superName.equals(nodeName)
                && optional.isPresent()
                && (new InstructionSearcher(optional.get().instructions, ICONST_M1, IASTORE, PUTFIELD)).match();
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<MethodNode> methodNode =
                classNode.methods.stream().filter(m -> m.name.equals("<init>")).findFirst();

        if (methodNode.isEmpty()) return;

        MethodNode constructor = methodNode.get();

        /**
         * ICONST_0
         *     ICONST_M1
         *     IASTORE
         *     PUTFIELD ItemStorage.f : [I
         */
        InstructionSearcher idInstruction =
                new InstructionSearcher(constructor.instructions, ICONST_0, ICONST_M1, IASTORE, PUTFIELD);

        if (idInstruction.match()) {
            List<AbstractInsnNode[]> matches = idInstruction.getMatches();

            List<AbstractInsnNode> match = idInstruction.getFirstMatch();

            if (match != null) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) match.get(3);

                if (fieldInsnNode.desc.equals("[I")) {
                    addField("ids", fieldInsnNode);
                }
            }
        }

        if (getFields().isEmpty()) return;

        for (FieldNode fieldNode : classNode.fields) {
            if (fieldNode.desc.equals("[I")
                    && !fieldNode.name.equals(getField("ids").getObfuscatedName())) {
                addField("stackSizes", fieldNode);
                break;
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
