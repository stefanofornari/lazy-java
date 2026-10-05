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
