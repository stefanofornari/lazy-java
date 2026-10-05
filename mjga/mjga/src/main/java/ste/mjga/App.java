/**
 * Copyright 2026 the original author or authors from the lazy-java project
 * (https://github.com/stefanofornari/lazy-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
