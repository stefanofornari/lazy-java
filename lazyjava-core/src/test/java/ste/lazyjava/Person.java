package ste.lazyjava;

import ste.lazyjava.annotation.£;

/**
 * Example class that is annotated with @£
 * to generate factory methods automatically.
 */
@£
public class Person {
    public final String name;
    public final int age;

    public Person() {
        this("Unknown", 0);
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
