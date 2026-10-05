package ste.mjga;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes that should have constructor helper methods generated.
 * This annotation will be processed by the MClassProcessor to generate
 * static factory methods that mimic constructor calls with simpler syntax.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface MClass {
    // Marker annotation - no elements needed
}