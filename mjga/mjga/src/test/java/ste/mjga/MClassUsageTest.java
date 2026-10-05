package ste.mjga;

import org.junit.jupiter.api.Test;

/**
 * Test demonstrating the actual usage of @MClass annotation.
 * This shows the working annotation processor functionality.
 */
class MClassUsageTest {

    @Test
    void should_demonstrate_working_syntax() {
        // Test the actual working functionality with the main Person class
        Person p1 = MJGA.Person();
        Person p2 = MJGA.Person("John", 30);
        
        assert p1.toString().equals("Person{name='Unknown', age=0}");
        assert p2.toString().equals("Person{name='John', age=30}");
    }
}