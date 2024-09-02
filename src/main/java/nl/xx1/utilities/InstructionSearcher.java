package nl.xx1.utilities;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class InstructionSearcher {
    private final InsnList instructions;
    private final int[] opcodes;
    private final List<AbstractInsnNode[]> matches = new ArrayList<>();

    public InstructionSearcher(InsnList instructions, int... opcodes) {
        this.instructions = instructions;
        this.opcodes = opcodes;
    }

    public boolean match() {
        AbstractInsnNode currentNode = instructions.getFirst();
        int matchIndex = 0;
        // We store each found node into this array, until the pattern breaks
        AbstractInsnNode[] nodes = new AbstractInsnNode[opcodes.length];

        while (currentNode != null) {
            // If we have a match, we store the node into our array
            // Then we shift one index to the right.
            if (matches(currentNode, opcodes[matchIndex])) {
                nodes[matchIndex] = currentNode;
                matchIndex++;
            } else {
                // If the pattern is broken we reset our structure.
                matchIndex = 0;
                nodes = new AbstractInsnNode[opcodes.length];
            }

            // If we have found a full match we'll add it into our array and reset the index
            if (matchIndex == opcodes.length) {
                matchIndex = 0;
                matches.add(nodes);
            }

            // Lastly we proceed our loop, until we reached the end of our loop.
            currentNode = currentNode.getNext();
        }

        return !matches.isEmpty();
    }

    public List<AbstractInsnNode[]> getMatches() {
        return matches;
    }

    private boolean matches(AbstractInsnNode insnNode, int opcode) {
        return insnNode.getOpcode() == opcode || opcode == -1;
    }

    public List<AbstractInsnNode> getFirstMatch() {
        if (matches.isEmpty()) return null;

        return List.of(matches.getFirst());
    }
}
