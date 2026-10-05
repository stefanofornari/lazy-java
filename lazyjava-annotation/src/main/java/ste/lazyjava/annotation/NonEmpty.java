package ste.lazyjava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a CharSequence parameter as non-empty (not null, not empty).
 *
 * <p>The generated validator will call:
 * {@code Safe.requireNonEmpty(param, "paramName")}
 *
 * <p>Usage:
 * <pre>
 * public void save(@NonEmpty String filename) { ... }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonEmpty {
}
