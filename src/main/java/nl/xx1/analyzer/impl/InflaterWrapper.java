package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class InflaterWrapper extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.superName.equals("java/lang/Object") && fieldCount(classNode, f -> f.desc.equals("Ljava/util/zip/Inflater;")) == 1;
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (FieldNode fieldNode : classNode.fields) {
            if (fieldNode.desc.equals("Ljava/util/zip/Inflater;")) {
                addField("inflator", fieldNode);
                break;
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            InstructionSearcher is = new InstructionSearcher(methodNode.instructions,
                    ALOAD,
                    GETFIELD,
                    LDC,
                    ALOAD,
                    GETFIELD);

            if (is.match()) {
                addMethod("decompress", methodNode);
            }
        }
    }
}
