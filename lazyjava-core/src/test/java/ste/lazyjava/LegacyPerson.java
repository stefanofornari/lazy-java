package ste.lazyjava;

import ste.lazyjava.annotation.LClass;

/**
 * Example class that uses the @LClass alias for @£.
 */
@LClass
public class LegacyPerson {
    private String name;
    private int age;

    public LegacyPerson() {
        this("Unknown", 0);
    }

    public LegacyPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
