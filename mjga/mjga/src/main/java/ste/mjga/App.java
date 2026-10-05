package ste.mjga;

import static ste.mjga.MJGA.*;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println( "Demonstrating clean constructor syntax:" );

        // This SHOULD work with static import but supposedly doesn't:
        Person p5 = Person();
        Person p6 = Person("Charlie", 40);

        System.out.println( "Created from static import: " + p5 );
        System.out.println( "Created from static import: " + p6 );
    }
}
