package nl.xx1.analyzer.impl;

import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.Analyzer;
import org.objectweb.asm.tree.ClassNode;

@Analyzer(
        name = "Client",
        description = "The starting point of the OSRS game",
        runAfter = {"Node"})
public class Client extends AbstractAnalyzer {
    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.name.equals("client");
    }

    @Override
    public void matchFields(ClassNode classNode) {}
}
