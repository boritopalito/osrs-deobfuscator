package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.Analyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

@Analyzer(
        name = "Link",
        description = "Node in a linked list",
        runAfter = {"Node"})
public class Link extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        if (!classNode.superName.equals("java/lang/Object")) {
            return false;
        }

        long selfCount = fieldCount(classNode, f -> f.desc.equalsIgnoreCase(String.format("L%s;", classNode.name)));
        long staticMethodCount = methodCount(classNode, m -> Modifier.isStatic(m.access));

        return Modifier.isPublic(classNode.access)
                && classNode.fields.size() == 2
                && selfCount == 2
                && staticMethodCount == 0;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<MethodNode> optional =
                classNode.methods.stream().filter(m -> !m.name.equals("<init>")).findFirst();

        if (optional.isPresent()) {
            InstructionSearcher is = new InstructionSearcher(
                    optional.get().instructions,
                    Opcodes.ALOAD,
                    Opcodes.GETFIELD,
                    Opcodes.ALOAD,
                    Opcodes.GETFIELD,
                    Opcodes.PUTFIELD);

            if (is.match()) {
                List<AbstractInsnNode> firstMatch = is.getFirstMatch();
                addField("next", (FieldInsnNode) firstMatch.get(1));
                addField("previous", (FieldInsnNode) firstMatch.get(3));
            }

            addMethod("unlink", optional.get());
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {
        // We already matched everything
    }
}
