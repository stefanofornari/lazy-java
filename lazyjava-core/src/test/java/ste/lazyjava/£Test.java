package ste.lazyjava;

import org.junit.jupiter.api.Test;
import static ste.lazyjava.£.Person;

class £Test {

    @Test
    void £_annotation_should_compile_without_errors() {
        Person instance1 = new Person();
        Person instance2 = new Person("test", 25);
        
        assert instance1 != null;
        assert instance2 != null;
    }

    @Test
    void should_use_generated_factory_methods() {
        var instance1 = Person();
        var instance2 = Person("test", 25);

        assert instance1 != null;
        assert instance2 != null;
    }
}
