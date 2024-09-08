package nl.xx1.analyzer.impl;

import java.lang.reflect.Modifier;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.utilities.InstructionSearcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ClientPreferences extends AbstractAnalyzer implements Opcodes {
    @Override
    public boolean canRun(ClassNode classNode) {
        Optional<MethodNode> optional =
                classNode.methods.stream().filter(m -> m.name.equals("<init>")).findFirst();

        if (optional.isEmpty()) {
            return false;
        }

        InstructionSearcher is =
                new InstructionSearcher(optional.get().instructions, LDC, INVOKEVIRTUAL, I2D, LDC, DDIV, PUTFIELD);

        return classNode.superName.equals("java/lang/Object") && Modifier.isPublic(classNode.access) && is.match();
    }

    @Override
    public void matchFields(ClassNode classNode) {
        Optional<MethodNode> optional =
                classNode.methods.stream().filter(m -> m.name.equals("<init>")).findFirst();

        if (optional.isEmpty()) {
            return;
        }

        MethodNode methodNode = optional.get();

        InstructionSearcher brightnessPattern =
                new InstructionSearcher(methodNode.instructions, ALOAD, ICONST_0, PUTFIELD, GOTO);

        if (brightnessPattern.match()) {
            FieldInsnNode brightnessPField =
                    (FieldInsnNode) brightnessPattern.getFirstMatch().get(2);
            addField("brightness", brightnessPField);
        }
    }

    @Override
    public void matchMethods(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            InstructionSearcher getBrightnessPattern =
                    new InstructionSearcher(method.instructions, ALOAD, GETFIELD, IRETURN);

            if (getBrightnessPattern.match()) {
                FieldInsnNode brightnessPutField =
                        (FieldInsnNode) getBrightnessPattern.getFirstMatch().get(1);
                if (brightnessPutField.name.equals(getField("brightness").getObfuscatedName())) {
                    addMethod("isBrightness", method);
                }
            }
        }
    }
}
