package ste.lazyjava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class for lazy static factory generation.
 *
 * <p>The processor generates a package-level {@code £} interface with static
 * factory methods that delegate to the annotated class's constructors.
 *
 * <p>Usage:
 * <pre>
 * {@code @£}
 * public class Person {
 *     public Person() {}
 *     public Person(String name, int age) {}
 * }
 *
 * // Generated in the same package:
 * // public interface £ {
 * //     public static Person Person() { return new Person(); }
 * //     public static Person Person(String name, int age) { return new Person(name, age); }
 * // }
 *
 * // Usage:
 * Person p = £.Person();
 * Person p2 = £.Person("John", 30);
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface £ {
}
