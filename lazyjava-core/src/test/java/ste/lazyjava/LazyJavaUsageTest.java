package ste.lazyjava;

import org.junit.jupiter.api.Test;
import static ste.lazyjava.£.Person;

class LazyJavaUsageTest {

    @Test
    void should_use_lazyjava_factory_methods() {
        var p1 = Person();
        var p2 = Person("Jane", 25);
        
        assert p1.toString().equals("Person{name='Unknown', age=0}");
        assert p2.toString().equals("Person{name='Jane', age=25}");
    }
}
