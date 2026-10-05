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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static ste.lazyjava.£.LegacyPerson;

class £AliasTest {

    @Test
    void lclass_alias_should_generate_factory_methods() {
        var p = LegacyPerson("Alias", 99);
        then(p.getName()).isEqualTo("Alias");
        then(p.getAge()).isEqualTo(99);
    }
}
