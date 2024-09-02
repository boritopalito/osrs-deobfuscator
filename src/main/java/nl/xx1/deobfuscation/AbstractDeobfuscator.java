package nl.xx1.deobfuscation;

import java.util.List;
import org.objectweb.asm.tree.ClassNode;

public abstract class AbstractDeobfuscator implements Deobfuscator {
    protected final List<ClassNode> classNodes;

    public AbstractDeobfuscator(List<ClassNode> classNodes) {
        this.classNodes = classNodes;
    }
}
