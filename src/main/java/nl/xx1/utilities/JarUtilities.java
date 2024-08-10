package nl.xx1.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class JarUtilities {
    public static List<ClassNode> loadClassNodes(File file) {
        List<ClassNode> classNodes = new ArrayList<>();

        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                ClassReader classReader = new ClassReader(jarFile.getInputStream(entry));
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, ClassReader.SKIP_FRAMES);
                classNodes.add(classNode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classNodes;
    }

    /**
     * Recompute MAXS and MAXLOCALS for each class and method.
     */
    public static void recomputeMaxsForClasses(List<ClassNode> classes) {
        // Iterate through each class
        for (ClassNode classNode : classes) {
            // Create a ClassWriter with COMPUTE_MAXS option
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            try {
                // Iterate through each method in the class
                for (MethodNode methodNode : classNode.methods) {
                    try {
                        // Create a MethodVisitor that recomputes MAXS and MAXLOCALS
                        MethodVisitor methodVisitor = classWriter.visitMethod(
                                methodNode.access,
                                methodNode.name,
                                methodNode.desc,
                                methodNode.signature,
                                methodNode.exceptions.toArray(new String[0]));

                        // Accept the MethodVisitor to recompute MAXS and MAXLOCALS
                        methodNode.accept(methodVisitor);
                    } catch (Exception e) {
                        // System.err.println("Error processing method " + methodNode.name + " in class " +
                        // classNode.name + ": " + e.getMessage());
                    }
                }

                // Convert the modified ClassNode back to a byte array
                classNode.accept(classWriter);
            } catch (Exception e) {
                // System.err.println("Error processing class " + classNode.name + ": " + e.getMessage());
            }
        }
    }

    public static void saveClassesToDisk(List<ClassNode> classes, String outputDirectory) {
        Path dir = Paths.get(outputDirectory);
        if (Files.exists(dir)) {
            try {
                deleteDirectoryRecursively(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Failed to create output directory: " + e.getMessage());
            return;
        }

        for (ClassNode classNode : classes) {
            try {
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(classWriter);
                byte[] classBytes = classWriter.toByteArray();

                String className = classNode.name.replace('/', '_') + ".class";
                String filePath = Paths.get(outputDirectory, className).toString();

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(classBytes);
                }
            } catch (IOException e) {
                System.err.printf("Failed to save class %s: %s\n", classNode.name, e.getMessage());
            }
        }
    }

    public static void deleteDirectoryRecursively(Path directory) throws IOException {
        try (Stream<Path> walk = Files.walk(directory)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new IOException("Failed to delete directory: " + directory, e);
        }
    }
}
