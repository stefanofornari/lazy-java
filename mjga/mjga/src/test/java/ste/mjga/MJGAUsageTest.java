package ste.mjga;

import org.junit.jupiter.api.Test;

/**
 * Test demonstrating the actual usage of the MJGA helper class.
 */
class MJGAUsageTest {

    @Test
    void should_use_mjga_factory_methods() {
        // Test the factory methods from the main Person class
        Person p1 = MJGA.Person();
        Person p2 = MJGA.Person("Jane", 25);
        
        assert p1.toString().equals("Person{name='Unknown', age=0}");
        assert p2.toString().equals("Person{name='Jane', age=25}");
    }
}