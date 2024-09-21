package nl.xx1.hooks;

import java.util.List;
import nl.xx1.analyzer.AbstractAnalyzer;

public class HookedClass {
    private final String name;
    private final String obfuscatedName;
    private final String superClass;
    private final int access;
    private final List<HookedField> fields;
    private final List<HookedMethod> methods;

    private HookedClass(
            String className, String obfuscatedName, String superClass, int access, List<HookedField> fields, List<HookedMethod> methods) {
        this.name = className;
        this.obfuscatedName = obfuscatedName;
        this.superClass = superClass;
        this.access = access;
        this.fields = fields;
        this.methods = methods;
    }

    public static HookedClass of(AbstractAnalyzer analyzer) {
        List<HookedField> hookedFieldList =
                analyzer.getFields().stream().map(HookedField::of).toList();

        List<HookedMethod> hookedMethodList =
                analyzer.getMethods().stream().map(HookedMethod::of).toList();

        return new HookedClass(
                analyzer.getClass().getSimpleName(),
                analyzer.getClassNode().name,
                analyzer.getClassNode().superName,
                analyzer.getClassNode().access,
                hookedFieldList,
                hookedMethodList);
    }

    public String getName() {
        return name;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getSuperClass() {
        return superClass;
    }

    public int getAccess() {
        return access;
    }

    public List<HookedField> getFields() {
        return fields;
    }

    public List<HookedMethod> getMethods() {
        return methods;
    }
}
