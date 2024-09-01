package nl.xx1.deobfuscation.impl;

import java.lang.reflect.Modifier;
import java.util.*;
import nl.xx1.deobfuscation.AbstractDeobfuscator;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class UnusedMethods extends AbstractDeobfuscator {
    public UnusedMethods(List<ClassNode> classNodes) {
        super(classNodes);
    }

    @Override
    public void deobfuscate() {
        int removed = 0;
        Set<String> referencedMethods = getReferencedMethod();

        for (ClassNode classNode : this.classNodes) {
            Iterator<MethodNode> iterator = classNode.methods.iterator();

            while (iterator.hasNext()) {
                MethodNode methodNode = iterator.next();

                String identifier = String.format("%s.%s%s", classNode.name, methodNode.name, methodNode.desc);

                if (!referencedMethods.contains(identifier)) {
                    iterator.remove();
                    removed++;
                }
            }
        }

        System.out.printf("[- %s removed %d methods -]%n", getClass().getSimpleName(), removed);
    }

    private Set<String> getReferencedMethod() {
        Set<String> referencedMethods = new HashSet<>();

        for (ClassNode classNode : this.classNodes) {
            for (MethodNode methodNode : classNode.methods) {
                String methodName = methodNode.name;
                if (methodName.equals("<init>") || methodName.equals("<clinit>")) {
                    referencedMethods.add(String.format("%s.%s%s", classNode.name, methodName, methodNode.desc));
                }

                for (AbstractInsnNode instruction : methodNode.instructions) {
                    if (instruction instanceof MethodInsnNode methodInsnNode) {

                        if (methodInsnNode.owner.equals(classNode.name)) {
                            referencedMethods.add(String.format(
                                    "%s.%s%s", methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc));
                        } else {
                            //                            System.out.println(methodInsnNode.getOpcode());
                            //                            System.out.println(String.format(
                            //                                    "%s.%s%s", methodInsnNode.owner, methodInsnNode.name,
                            // methodInsnNode.desc));
                            String reference = getRealOwner(methodInsnNode);

                            if (reference != null) {
                                referencedMethods.add(
                                        String.format("%s.%s%s", reference, methodInsnNode.name, methodInsnNode.desc));
                            }
                        }
                    }
                }
            }
        }
        return referencedMethods;
    }

    private String getRealOwner(MethodInsnNode methodInsnNode) {
        String identifier = String.format("%s%s", methodInsnNode.name, methodInsnNode.desc);
        ClassNode classNode = findClass(methodInsnNode.owner).orElse(null);

        if (classNode == null) {
            return null;
        }

        // Check the class and its superclasses
        String result = findMethodInHierarchy(classNode, identifier);
        if (result != null) {
            return result;
        }

        // Check interfaces
        result = findMethodInInterfaces(classNode, identifier, new HashSet<>());
        return result;
    }

    private String findMethodInHierarchy(ClassNode classNode, String identifier) {
        while (classNode != null) {
            for (MethodNode methodNode : classNode.methods) {
                String method = String.format("%s%s", methodNode.name, methodNode.desc);
                if (identifier.equals(method)) {
                    return classNode.name;
                }
            }

            classNode = findClass(classNode.superName).orElse(null);
        }
        return null;
    }

    private String findMethodInInterfaces(ClassNode classNode, String identifier, Set<String> visitedInterfaces) {
        if (classNode == null || visitedInterfaces.contains(classNode.name)) {
            return null;
        }

        visitedInterfaces.add(classNode.name);

        // Check methods in this interface
        for (MethodNode methodNode : classNode.methods) {
            String method = String.format("%s%s", methodNode.name, methodNode.desc);
            if (identifier.equals(method)) {
                return classNode.name;
            }
        }

        // Check superinterfaces
        for (String interfaceName : classNode.interfaces) {
            ClassNode interfaceNode = findClass(interfaceName).orElse(null);
            String result = findMethodInInterfaces(interfaceNode, identifier, visitedInterfaces);
            if (result != null) {
                return result;
            }
        }

        // If this is a class (not an interface), check its interfaces
        if (!Modifier.isInterface(classNode.access)) {
            for (String interfaceName : classNode.interfaces) {
                ClassNode interfaceNode = findClass(interfaceName).orElse(null);
                String result = findMethodInInterfaces(interfaceNode, identifier, visitedInterfaces);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private Optional<ClassNode> findClass(String name) {
        return classNodes.stream().filter(c -> c.name.equals(name)).findFirst();
    }
}
