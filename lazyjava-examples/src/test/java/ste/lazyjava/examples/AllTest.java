package ste.lazyjava.examples;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;

import static ste.lazyjava.examples.£.*;

public class AllTest
{
    @Test
    public void all()
    {
        System.out.println( "Hello World!" );
        System.out.println( "Demonstrating clean constructor syntax:" );

        Person p5 = Person();
        Person p6 = Person("Charlie", 40);

        then(p5.name()).isEqualTo("Unknown");
        then(p5.age()).isZero();

        then(p6.name()).isEqualTo("Charlie");
        then(p6.age()).isEqualTo(40);

        thenThrownBy(() -> {
            p5.age(null);
        }).isInstanceOf(IllegalArgumentException.class);

        thenThrownBy(() -> {
            p6.name(" \t \n");
        });

        System.out.println( "Created: " + p5 );
        System.out.println( "Created: " + p6 );
    }
}
