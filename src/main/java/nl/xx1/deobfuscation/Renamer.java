package nl.xx1.deobfuscation;

import java.util.List;
import nl.xx1.Field;
import nl.xx1.Method;
import nl.xx1.analyzer.AbstractAnalyzer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class Renamer {
    private int totalClasses = 0;

    public void execute(List<ClassNode> classNodes, List<AbstractAnalyzer> analyzers) {
        totalClasses = classNodes.size();
        List<AbstractAnalyzer> fAnalyzers =
                analyzers.stream().filter(a -> a.getClassNode() != null).toList();

        for (AbstractAnalyzer analyzer : fAnalyzers) {
            updateClassName(classNodes, analyzer);
        }
    }

    private void updateClassName(List<ClassNode> classNodes, AbstractAnalyzer analyzer) {
        String obfuscatedName = analyzer.getClassNode().name;
        String deobfuscatedName = analyzer.getClass().getSimpleName();

        String obfuscatedDesc = String.format("L%s;", obfuscatedName);
        String deobfuscatedDesc = String.format("L%s;", deobfuscatedName);

        for (ClassNode classNode : classNodes) {

            // We re-name the obfuscated class to our deobfuscated className
            if (classNode.name.equals(obfuscatedName)) {
                classNode.name = analyzer.getClass().getSimpleName();
            }

            // For each class that extends us, we rename the parent class as wel
            if (classNode.superName.equals(obfuscatedName)) {
                classNode.superName = analyzer.getClass().getSimpleName();
            }

            for (InnerClassNode innerClassNode : classNode.innerClasses) {
                if (innerClassNode.name.equals(obfuscatedName)) {
                    innerClassNode.name = deobfuscatedName;
                }
            }

            // We set each field to new desc
            for (FieldNode fieldNode : classNode.fields) {

                if (fieldNode.desc.equals(obfuscatedDesc)) {
                    fieldNode.desc = deobfuscatedDesc;
                }

                if (fieldNode.desc.equals(String.format("[%s", obfuscatedDesc))) {
                    fieldNode.desc = fieldNode.desc.replace(obfuscatedDesc, deobfuscatedDesc);
                }
            }

            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.desc.equals(String.format("()%s", obfuscatedDesc))) {
                    methodNode.desc = String.format("()%s", deobfuscatedDesc);
                }

                if (methodNode.desc.contains(obfuscatedDesc)) {
                    methodNode.desc = methodNode.desc.replace(obfuscatedDesc, deobfuscatedDesc);
                }

                for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                    if (abstractInsnNode instanceof FieldInsnNode fieldInsnNode) {
                        if (fieldInsnNode.owner.equals(obfuscatedName)) {
                            fieldInsnNode.owner = deobfuscatedName;
                        }

                        if (fieldInsnNode.desc.contains(obfuscatedDesc)) {
                            fieldInsnNode.desc = fieldInsnNode.desc.replace(obfuscatedDesc, deobfuscatedDesc);
                        }
                    }

                    if (abstractInsnNode instanceof MethodInsnNode methodInsnNode) {
                        if (methodInsnNode.owner.equals(obfuscatedName)) {
                            methodInsnNode.owner = deobfuscatedName;
                        }

                        if (methodInsnNode.desc.contains(obfuscatedDesc)) {
                            methodInsnNode.desc = methodInsnNode.desc.replace(obfuscatedDesc, deobfuscatedDesc);
                        }
                    }
                }
            }

            updateFieldReferences(classNode, analyzer);
            updateMethodReferences(classNode, analyzer);
        }
    }

    private void updateMethodReferences(ClassNode classNode, AbstractAnalyzer analyzer) {
        for (Method method : analyzer.getMethods()) {
            for (MethodNode methodNode : classNode.methods) {
                if (classNode.equals(analyzer.getClassNode()) && methodNode.name.equals(method.getObfuscatedName())) {
                    methodNode.name = method.getName();
                }

                for (AbstractInsnNode insnNode : methodNode.instructions) {
                    if (insnNode instanceof MethodInsnNode methodInsnNode) {
                        if (methodInsnNode.owner.equals(analyzer.getClassNode().name)
                                && methodInsnNode.name.equals(method.getObfuscatedName())) {
                            methodInsnNode.name = method.getName();
                        }

                        if (methodInsnNode.owner.equals(analyzer.getClass().getSimpleName())) {
                            methodInsnNode.name = method.getName();
                        }
                    }
                }
            }
        }
    }

    private void updateFieldReferences(ClassNode classNode, AbstractAnalyzer analyzer) {
        for (Field field : analyzer.getFields()) {
            for (FieldNode fieldNode : classNode.fields) {
                if (classNode.equals(analyzer.getClassNode()) && fieldNode.name.equals(field.getObfuscatedName())) {
                    fieldNode.name = field.getName();
                }
            }

            for (MethodNode methodNode : classNode.methods) {
                for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                    if (abstractInsnNode instanceof FieldInsnNode fieldInsnNode) {
                        if (fieldInsnNode.owner.equals(analyzer.getClass().getSimpleName())) {
                            if (fieldInsnNode.name.equals(field.getObfuscatedName())) {
                                fieldInsnNode.name = field.getName();
                            }
                        }
                    }

                    if (abstractInsnNode instanceof TypeInsnNode typeInsnNode) {
                        if (typeInsnNode.getOpcode() == Opcodes.NEW
                                && typeInsnNode.desc.equals(analyzer.getClassNode().name)) {
                            typeInsnNode.desc = analyzer.getClass().getSimpleName();
                        }
                    }
                }
            }
        }
    }
}
