package nl.xx1.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import nl.xx1.Field;
import nl.xx1.Method;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class AbstractAnalyzer implements Opcodes {
    private ClassNode classNode;
    private final List<Field> fields = new ArrayList<>();
    private final List<Method> methods = new ArrayList<>();

    protected AnalyzerContext context;

    public abstract boolean canRun(ClassNode classNode);

    public void execute(ClassNode classNode) {
        long start = System.currentTimeMillis();
        this.classNode = classNode;
        matchFields(classNode);
        matchMethods(classNode);
        long speed = System.currentTimeMillis() - start;
    }

    public abstract void matchFields(ClassNode classNode);

    public abstract void matchMethods(ClassNode classNode);

    public void addField(String name, FieldInsnNode fieldInsnNode) {
        Optional<FieldNode> optionalFieldNode = classNode.fields.stream()
                .filter(f -> f.name.equals(fieldInsnNode.name))
                .findFirst();

        if (optionalFieldNode.isEmpty()) throw new RuntimeException("This fieldInsnNode doesnt belong to this class");

        addField(name, optionalFieldNode.get());
    }

    public void addField(String name, FieldNode fieldNode) {
        if (fieldNode == null) {
            return;
        }
        int multiplier = 1;

        if (fieldNode.desc.equals("I")) {
            multiplier = context.getMultiplier(String.format("%s.%s", classNode.name, fieldNode.name));
        }

        Field field = new Field.Builder()
                .fieldNode(fieldNode)
                .name(name)
                .obfuscatedName(fieldNode.name)
                .multiplier(multiplier)
                .build();

        if (fields.contains(field)) return;

        fields.add(field);
    }

    public void addMethod(String name, MethodNode methodNode) {
        Method method = new Method.Builder()
                .methodNode(methodNode)
                .obfuscatedName(methodNode.name)
                .name(name)
                .build();
        methods.add(method);
    }

    public Field getField(String name) {
        return fields.stream().filter(f -> f.getName().equals(name)).findAny().orElseThrow();
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public long fieldCount(ClassNode classNode, Predicate<FieldNode> condition) {
        return classNode.fields.stream().filter(condition).count();
    }

    public long fieldCount(Predicate<FieldNode> condition) {
        return fieldCount(this.classNode, condition);
    }

    public long methodCount(ClassNode classNode, Predicate<MethodNode> condition) {
        return classNode.methods.stream().filter(condition).count();
    }

    public long methodCount(Predicate<MethodNode> condition) {
        return methodCount(this.classNode, condition);
    }

    public void print() {
        System.out.println(this);

        for (Field field : fields) {
            System.out.println(field);
        }

        for (Method method : methods) {
            System.out.println(method);
        }
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public String getDescription() {
        return String.format("L%s;", classNode.name);
    }

    @Override
    public String toString() {
        return String.format(
                "[- %s identified as %s extends %s -]",
                getClass().getSimpleName(), classNode.name, classNode.superName);
    }

    public void setContext(AnalyzerContext context) {
        this.context = context;
    }
}
