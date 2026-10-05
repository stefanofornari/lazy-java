package ste.mjga;

import org.junit.jupiter.api.Test;

/**
 * Test for the MJGA annotation processor.
 * This test verifies that the processor generates the expected factory methods.
 */
class MJGAProcessorTest {

    @Test
    void generated_mjga_class_should_work() {
        // Test the generated MJGA class with the main Person class
        Person p1 = MJGA.Person();
        Person p2 = MJGA.Person("John", 30);
        
        assert p1.toString().equals("Person{name='Unknown', age=0}");
        assert p2.toString().equals("Person{name='John', age=30}");
    }
}