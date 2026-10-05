package ste.lazyjava;

import org.junit.jupiter.api.Test;
import static ste.lazyjava.£.Person;

class LazyJavaProcessorTest {

    @Test
    void generated_lazyjava_class_should_work() {
        var p1 = Person();
        var p2 = Person("John", 30);
        
        assert p1.toString().equals("Person{name='Unknown', age=0}");
        assert p2.toString().equals("Person{name='John', age=30}");
    }
}
