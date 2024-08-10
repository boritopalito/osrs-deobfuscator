package nl.xx1.analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Analyzer {
    String name();

    String description();

    String[] runAfter() default {};

    String[] runBefore() default {};
}
