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
package ste.lazyjava.examples;

import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonNull;
import ste.lazyjava.annotation.£;

/**
 * Example class that is annotated with @£
 * to generate factory methods automatically.
 */
@£
public class Person {
    private String name;
    private int age;

    public Person() {
        this("Unknown", 0);
    }

    public Person(@NonBlank String name) {
        this(name, 0);
    }

    public Person(@NonBlank String name, @NonNull Integer age) {
        this.name = name;
        this.age = age;
    }

    public String name() {
        return name;
    }

    public void name(@NonBlank String name) {
        this.name = name;
    }

    public Integer age() {
       return age;
    }

    public void age(@NonNull Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}