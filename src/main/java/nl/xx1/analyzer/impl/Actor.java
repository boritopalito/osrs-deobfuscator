package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Actor extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        ClassNode rendererNode = context.getAnalyzer("RendererNode").getClassNode();

        if (rendererNode == null) {
            return false;
        }

        String rendererNodeName = rendererNode.name;

        return classNode.superName.equals(rendererNodeName)
                && classNode.methods.stream().anyMatch(method -> {
                    InstructionSearcher is = new InstructionSearcher(method.instructions, ALOAD, ACONST_NULL, PUTFIELD);
                    return is.match() && ((FieldInsnNode) is.getFirstMatch().get(2)).desc.equals("Ljava/lang/String;");
                });
    }

    @Override
    public void matchFields(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {

            // ALOAD 0
            //    LDC -184820238
            //    INVOKEVIRTUAL Actor.ab (I)I
            //    ALOAD 0
            //    ICONST_0
            //    INVOKEVIRTUAL Actor.ay (B)I
            //    INVOKESPECIAL nv.<init> (III)V
            //    ARETURN
            InstructionSearcher is = new InstructionSearcher(
                    methodNode.instructions, LDC, INVOKEVIRTUAL, ALOAD, ICONST_0, INVOKEVIRTUAL);

            if (is.match()) {
                System.out.println(methodNode.name);
            }
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {}
}
