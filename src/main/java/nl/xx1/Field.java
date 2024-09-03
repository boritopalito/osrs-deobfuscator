package nl.xx1;

import java.util.Objects;
import org.objectweb.asm.tree.FieldNode;

public class Field {
    private final String name;
    private final String obfuscatedName;
    private final FieldNode fieldNode;
    private final int multiplier;

    private Field(Builder builder) {
        this.name = builder.name;
        this.obfuscatedName = builder.obfuscatedName;
        this.fieldNode = builder.fieldNode;
        this.multiplier = builder.multiplier;
    }

    public String getName() {
        return name;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public FieldNode getFieldNode() {
        return fieldNode;
    }

    public int getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(String.format("\t[- Field '%s' identified as %s -]", name, obfuscatedName));
        if (multiplier > 1) {
            response.append(String.format("\t[* %d]", multiplier));
        }
        return response.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name) && Objects.equals(obfuscatedName, field.obfuscatedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, obfuscatedName);
    }

    public static class Builder {
        private String name;
        private String obfuscatedName;
        private FieldNode fieldNode;
        private int multiplier = 1;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder obfuscatedName(String obfuscatedName) {
            this.obfuscatedName = obfuscatedName;
            return this;
        }

        public Builder fieldNode(FieldNode fieldNode) {
            this.fieldNode = fieldNode;
            return this;
        }

        public Builder multiplier(int multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public Field build() {
            return new Field(this);
        }
    }
}
