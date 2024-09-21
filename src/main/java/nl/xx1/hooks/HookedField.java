package nl.xx1.hooks;

import java.lang.reflect.Modifier;
import nl.xx1.Field;

public class HookedField {
    private final String name;
    private final String obfuscatedName;
    private final String descriptor;
    private final boolean isStatic;

    private HookedField(String name, String obfuscatedName, String descriptor, boolean isStatic) {
        this.name = name;
        this.obfuscatedName = obfuscatedName;
        this.descriptor = descriptor;
        this.isStatic = isStatic;
    }

    public static HookedField of(Field field) {
        return new HookedField(
                field.getName(),
                field.getObfuscatedName(),
                field.getFieldNode().desc,
                Modifier.isStatic(field.getFieldNode().access));
    }

    public String getName() {
        return name;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
