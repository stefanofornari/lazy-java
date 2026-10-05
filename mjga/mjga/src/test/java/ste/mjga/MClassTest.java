package ste.mjga;

import org.junit.jupiter.api.Test;

/**
 * Test for the MClass annotation functionality.
 * This test verifies that the annotation can be applied to classes without compilation errors.
 */
class MClassTest {

    @Test
    void m_class_annotation_should_compile_without_errors() {
        // Test that we can create Person instances using traditional constructors
        Person instance1 = new Person();
        Person instance2 = new Person("test", 25);
        
        // Basic verification that instances can be created
        assert instance1 != null;
        assert instance2 != null;
    }

    @Test
    void should_use_generated_factory_methods() {
        // Test that we can create Person instances using the generated MJGA factory methods
        Person instance1 = MJGA.Person();
        Person instance2 = MJGA.Person("test", 25);

        assert instance1 != null;
        assert instance2 != null;
    }
}