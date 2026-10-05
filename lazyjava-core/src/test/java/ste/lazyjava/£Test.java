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
package ste.lazyjava;

import ste.lazyjava.processor.Person;
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
