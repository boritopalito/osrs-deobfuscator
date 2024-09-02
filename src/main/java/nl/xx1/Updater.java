package nl.xx1;

import java.io.File;
import java.util.List;
import java.util.Optional;
import nl.xx1.analyzer.AbstractAnalyzer;
import nl.xx1.analyzer.AnalyzerSorter;
import nl.xx1.analyzer.impl.Client;
import nl.xx1.analyzer.impl.Link;
import nl.xx1.analyzer.impl.Node;
import nl.xx1.deobfuscation.Deobfuscator;
import nl.xx1.deobfuscation.Renamer;
import nl.xx1.deobfuscation.impl.UnusedFields;
import nl.xx1.deobfuscation.impl.UnusedMethods;
import nl.xx1.utilities.JarUtilities;
import org.objectweb.asm.tree.ClassNode;

public class Updater {
    private final String path;

    public Updater(String path) {
        this.path = path;
    }

    private List<AbstractAnalyzer> getAnalyzers() {
        return List.of(new Client(), new Node(), new Link());
    }

    public void execute() {
        final AnalyzerSorter sorter = new AnalyzerSorter();
        final File file = new File(this.path);

        if (!file.exists()) {
            throw new RuntimeException(
                    String.format("The path (%s) does not exists, or the file can't be accessed.", path));
        }

        final List<ClassNode> classNodes = JarUtilities.loadClassNodes(file);

        if (classNodes.isEmpty()) {
            throw new RuntimeException("The .jar file you provided doesn't contain any classes.");
        }

        // ---
        final List<Deobfuscator> deobfuscators = List.of(new UnusedMethods(classNodes), new UnusedFields(classNodes));
        for (Deobfuscator deobfuscator : deobfuscators) {
            deobfuscator.deobfuscate();
            JarUtilities.recomputeMaxsForClasses(classNodes);
        }
        // ---

        final List<AbstractAnalyzer> analyzers = getAnalyzers();

        for (AbstractAnalyzer analyzer : analyzers) {
            Optional<ClassNode> optional =
                    classNodes.stream().filter(analyzer::canRun).findFirst();

            if (optional.isEmpty()) {
                continue;
            }

            ClassNode classNode = optional.get();
            analyzer.execute(classNode);
            analyzer.print();
        }

        Renamer renamer = new Renamer();
        renamer.execute(classNodes, analyzers);

        JarUtilities.recomputeMaxsForClasses(classNodes);
        JarUtilities.saveClassesToDisk(classNodes, String.format("deob-gamepacks/%s/", file.getName()));
    }

    public static void main(String[] args) {
        Updater updater = new Updater("gamepacks/osrs-209.jar");
        updater.execute();
    }
}
