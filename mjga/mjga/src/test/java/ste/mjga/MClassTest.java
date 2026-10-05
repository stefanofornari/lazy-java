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