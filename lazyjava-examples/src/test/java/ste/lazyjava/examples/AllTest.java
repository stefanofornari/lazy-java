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

import java.util.List;

import static ste.lazyjava.examples.£.*;

/**
 * Tests demonstrating @NonEmpty with Collections and arrays.
 */
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

    @Test
    public void nonEmptyCollectionsAndArrays()
    {
        System.out.println("Demonstrating @NonEmpty with Collections and arrays:");

        // Valid cases
        CollectionExample c1 = CollectionExample("valid", List.of("tag1", "tag2"), new String[]{"cat1"}, new int[]{1, 2, 3});
        then(c1.getTags()).containsExactly("tag1", "tag2");
        then(c1.getCategories()).containsExactly("cat1");
        then(c1.getScores()).containsExactly(1, 2, 3);

        // Test setters with valid values
        c1.setTags(List.of("new-tag"));
        c1.setCategories(new String[]{"new-cat"});
        c1.setScores(new int[]{42});
        then(c1.getTags()).containsExactly("new-tag");
        then(c1.getCategories()).containsExactly("new-cat");
        then(c1.getScores()).containsExactly(42);

        // Null collection should throw
        thenThrownBy(() -> CollectionExample("test", null, new String[]{"cat"}, new int[]{1}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("tags must be non empty");

        // Empty collection should throw
        thenThrownBy(() -> CollectionExample("test", List.of(), new String[]{"cat"}, new int[]{1}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("tags must be non empty");

        // Null array should throw
        thenThrownBy(() -> CollectionExample("test", List.of("tag"), null, new int[]{1}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("categories must be non empty");

        // Empty array should throw
        thenThrownBy(() -> CollectionExample("test", List.of("tag"), new String[0], new int[]{1}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("categories must be non empty");

        // Null primitive array should throw
        thenThrownBy(() -> CollectionExample("test", List.of("tag"), new String[]{"cat"}, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("scores must be non empty");

        // Empty primitive array should throw
        thenThrownBy(() -> CollectionExample("test", List.of("tag"), new String[]{"cat"}, new int[0]))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("scores must be non empty");

        // Empty setter should throw
        thenThrownBy(() -> c1.setTags(List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("tags must be non empty");

        thenThrownBy(() -> c1.setCategories(new String[0]))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("categories must be non empty");

        thenThrownBy(() -> c1.setScores(new int[0]))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("scores must be non empty");

        System.out.println("Created: " + c1);
    }
}
