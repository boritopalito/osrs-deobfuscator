package nl.xx1.utilities;

import java.util.HashMap;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MultiplierFinder implements Opcodes {
    private final HashMap<String, Integer> multipliers = new HashMap<>();
    private final List<ClassNode> classNodes;

    public MultiplierFinder(List<ClassNode> classNodes) {
        this.classNodes = classNodes;
    }

    public void findMultipliers() {
        for (ClassNode classNode : classNodes) {
            for (MethodNode method : classNode.methods) {
                InstructionSearcher is = new InstructionSearcher(method.instructions, LDC, ALOAD, GETFIELD, IMUL);

                if (!is.match()) continue;

                List<AbstractInsnNode[]> matches = is.getMatches();

                for (AbstractInsnNode[] match : matches) {
                    Integer value = null;
                    String ref = null;

                    for (AbstractInsnNode abstractInsnNode : match) {
                        if (abstractInsnNode instanceof LdcInsnNode ldcInsnNode) {
                            try {
                                value = (Integer) ldcInsnNode.cst;
                            } catch (ClassCastException e) {
                                break;
                            }
                        }

                        if (abstractInsnNode instanceof FieldInsnNode fieldInsnNode) {
                            ref = String.format("%s.%s", fieldInsnNode.owner, fieldInsnNode.name);
                        }
                    }

                    if (value != null && ref != null) {
                        multipliers.put(ref, value);
                    }
                }
            }
        }
        System.out.println(multipliers);
    }
}
