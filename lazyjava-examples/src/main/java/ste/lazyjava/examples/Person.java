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