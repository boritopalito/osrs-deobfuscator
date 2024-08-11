package nl.xx1.deobfuscation;

import java.util.List;
import org.objectweb.asm.tree.ClassNode;

public interface Deobfuscator {
    void deobfuscate(List<ClassNode> classNodes);
}
