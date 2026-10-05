/**
 * Copyright 2026 the original author or authors from the lazy-java project
 * (https://github.com/stefanofornari/lazy-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
