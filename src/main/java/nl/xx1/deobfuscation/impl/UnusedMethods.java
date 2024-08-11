package nl.xx1.deobfuscation.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.xx1.deobfuscation.Deobfuscator;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class UnusedMethods implements Deobfuscator {
    @Override
    public void deobfuscate(List<ClassNode> classNodes) {}

    private Set<String> getReferencedMethod(List<ClassNode> classNodes) {
        Set<String> referencedMethods = new HashSet<>();

        for (ClassNode classNode : classNodes) {
            for (MethodNode methodNode : classNode.methods) {
                String methodName = methodNode.name;
                if (methodName.equals("<init>") || methodName.equals("<clinit>")) {
                    referencedMethods.add(String.format("%s.%s%s", classNode.name, methodName, methodNode.desc));
                }
            }
        }
    }
}
