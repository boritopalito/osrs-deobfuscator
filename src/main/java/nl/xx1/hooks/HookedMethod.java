package nl.xx1.hooks;

import java.lang.reflect.Modifier;
import nl.xx1.Method;

public class HookedMethod {
    private final String name;
    private final String obfuscatedName;
    private final String descriptor;
    private final boolean isStatic;

    private HookedMethod(String name, String obfuscatedName, String descriptor, boolean isStatic) {
        this.name = name;
        this.obfuscatedName = obfuscatedName;
        this.descriptor = descriptor;
        this.isStatic = isStatic;
    }

    public static HookedMethod of(Method method) {
        return new HookedMethod(
                method.getName(),
                method.getObfuscatedName(),
                method.getMethodNode().desc,
                Modifier.isStatic(method.getMethodNode().access));
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
