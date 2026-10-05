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