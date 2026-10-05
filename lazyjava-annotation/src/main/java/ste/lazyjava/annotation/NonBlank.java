package ste.lazyjava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a String parameter as non-blank (not null, not empty, not whitespace-only).
 *
 * <p>The generated validator will call:
 * {@code Safe.requireNonBlank(param, "paramName")}
 *
 * <p>Usage:
 * <pre>
 * public void send(@NonBlank String message) { ... }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonBlank {
}
