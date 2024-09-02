package nl.xx1.analyzer.impl;

import java.util.List;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.Analyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.tree.*;

@Analyzer(
        name = "LinkedList",
        description = "This class represents a custom implementation of a linked list",
        runAfter = {"Link"})
public class LinkedList extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        AbstractAnalyzer link = context.getAnalyzer("Link");
        String linkDesc = link.getDescription();
        long linkCount = fieldCount(classNode, f -> f.desc.equals(linkDesc));
        long fieldCount = classNode.fields.size();
        return linkCount == 2 && fieldCount == 2 && !classNode.name.equals(link.getClassNode().name);
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (!methodNode.name.equals("<init>")) continue;

            InstructionSearcher is = new InstructionSearcher(methodNode.instructions, ALOAD, GETFIELD, ALOAD);

            if (is.match()) {
                List<AbstractInsnNode[]> matches = is.getMatches();

                if (!matches.isEmpty()) {
                    AbstractInsnNode[] nodes = matches.getFirst();
                    FieldInsnNode fieldInsnNode = (FieldInsnNode) nodes[1];
                    addField("head", fieldInsnNode);
                    break;
                }
            }
        }

        for (FieldNode fieldNode : classNode.fields) {
            if (getFields().isEmpty()) continue;

            if (!fieldNode.name.equals(getField("head").getObfuscatedName())) {
                addField("pointer", fieldNode);
                break;
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {
        String linkName = context.getAnalyzer("Link").getClassNode().name;

        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.desc.equals(String.format("(L%s;)V", linkName))) {
                addMethod("addLast", methodNode);
                continue;
            }

            InstructionSearcher firstIs = new InstructionSearcher(methodNode.instructions, GETFIELD, GETFIELD, ASTORE);

            if (firstIs.match()) {
                addMethod("getFirst", methodNode);
            }

            InstructionSearcher nextIs = new InstructionSearcher(methodNode.instructions, ALOAD, GETFIELD, ASTORE);

            if (nextIs.match()) {
                addMethod("getNext", methodNode);
            }
        }
    }
}
