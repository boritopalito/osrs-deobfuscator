package nl.xx1;

import org.objectweb.asm.tree.MethodNode;

public class Method {
    private final String name;
    private final String obfuscatedName;
    private final MethodNode methodNode;

    private Method(Builder builder) {
        this.name = builder.name;
        this.obfuscatedName = builder.obfuscatedName;
        this.methodNode = builder.methodNode;
    }

    public String getName() {
        return name;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    @Override
    public String toString() {
        return String.format("\t[- Method '%s' identified as %s -]", name, obfuscatedName);
    }

    public static class Builder {
        private String name;
        private String obfuscatedName;
        private MethodNode methodNode;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder obfuscatedName(String obfuscatedName) {
            this.obfuscatedName = obfuscatedName;
            return this;
        }

        public Builder methodNode(MethodNode methodNode) {
            this.methodNode = methodNode;
            return this;
        }

        public Method build() {
            return new Method(this);
        }
    }
}
