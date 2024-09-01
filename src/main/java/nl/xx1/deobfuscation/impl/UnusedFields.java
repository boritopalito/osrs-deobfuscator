package nl.xx1.deobfuscation.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.xx1.deobfuscation.AbstractDeobfuscator;
import org.objectweb.asm.tree.*;

public class UnusedFields extends AbstractDeobfuscator {
    public UnusedFields(List<ClassNode> classNodes) {
        super(classNodes);
    }

    @Override
    public void deobfuscate() {
        int removed = 0;
        Set<String> referencedFields = getReferencedFields(classNodes);

        for (ClassNode classNode : classNodes) {
            Iterator<FieldNode> iterator = classNode.fields.iterator();

            while (iterator.hasNext()) {
                FieldNode fieldNode = iterator.next();

                String identifier = String.format("%s.%s%s", classNode.name, fieldNode.name, fieldNode.desc);

                if (!referencedFields.contains(identifier)) {
                    iterator.remove();
                    removed++;
                }
            }
        }

        System.out.printf("[- %s removed %d fields -]%n", getClass().getSimpleName(), removed);
    }

    private Set<String> getReferencedFields(List<ClassNode> classNodes) {
        Set<String> referencedFields = new HashSet<>();

        for (ClassNode classNode : classNodes) {
            for (MethodNode methodNode : classNode.methods) {
                for (AbstractInsnNode instruction : methodNode.instructions) {
                    if (instruction instanceof FieldInsnNode fieldInsnNode) {
                        referencedFields.add(
                                String.format("%s.%s%s", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc));
                    }
                }
            }
        }

        return referencedFields;
    }
}
