package ste.lazyjava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter as non-null.
 *
 * <p>At runtime, LazyJava injects a null guard into the annotated method/constructor
 * so that a null argument fails fast with {@code IllegalArgumentException}.
 *
 * <p>Usage:
 * <pre>
 * public void greet(@NonNull String name) { ... }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
}
