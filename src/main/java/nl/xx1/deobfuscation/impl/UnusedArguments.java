package nl.xx1.deobfuscation.impl;

import java.util.List;
import nl.xx1.deobfuscation.AbstractDeobfuscator;
import org.objectweb.asm.tree.ClassNode;

public class UnusedArguments extends AbstractDeobfuscator {
    public UnusedArguments(List<ClassNode> classNodes) {
        super(classNodes);
    }

    @Override
    public void deobfuscate() {}
}
