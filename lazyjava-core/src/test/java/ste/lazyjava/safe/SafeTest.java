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
package ste.lazyjava.safe;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class SafeTest {

    // --- ifNotNull ---

    @Test
    void if_not_null_should_execute_runnable_when_object_is_not_null() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Safe.ifNotNull("hello", () -> executed.set(true));

        then(executed.get()).isTrue();
    }

    @Test
    void if_not_null_should_not_execute_runnable_when_object_is_null() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Safe.ifNotNull(null, () -> executed.set(true));

        then(executed.get()).isFalse();
    }

    // --- ifNull (2 args) ---

    @Test
    void if_null_should_execute_runnable_when_object_is_null() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Safe.ifNull(null, () -> executed.set(true));

        then(executed.get()).isTrue();
    }

    @Test
    void if_null_should_not_execute_runnable_when_object_is_not_null() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Safe.ifNull("hello", () -> executed.set(true));

        then(executed.get()).isFalse();
    }

    // --- ifNull (3 args) ---

    @Test
    void if_null_should_execute_true_runnable_when_object_is_null() {
        AtomicBoolean trueExecuted = new AtomicBoolean(false);
        AtomicBoolean falseExecuted = new AtomicBoolean(false);

        Safe.ifNull(null, () -> trueExecuted.set(true), () -> falseExecuted.set(true));

        then(trueExecuted.get()).isTrue();
        then(falseExecuted.get()).isFalse();
    }

    @Test
    void if_null_should_execute_false_runnable_when_object_is_not_null() {
        AtomicBoolean trueExecuted = new AtomicBoolean(false);
        AtomicBoolean falseExecuted = new AtomicBoolean(false);

        Safe.ifNull("hello", () -> trueExecuted.set(true), () -> falseExecuted.set(true));

        then(trueExecuted.get()).isFalse();
        then(falseExecuted.get()).isTrue();
    }

    // --- safe ---

    @Test
    void safe_should_accept_consumer_when_object_is_not_null() {
        AtomicReference<String> result = new AtomicReference<>();

        Safe.safe("hello", result::set);

        then(result.get()).isEqualTo("hello");
    }

    @Test
    void safe_should_not_accept_consumer_when_object_is_null() {
        AtomicReference<String> result = new AtomicReference<>();

        Safe.safe(null, result::set);

        then(result.get()).isNull();
    }

    // --- requireNonNull ---

    @Test
    void require_non_null_should_pass_when_object_is_not_null() {
        Safe.requireNonNull("valid", "paramName");

        then(true).isTrue(); // Confirms no exception was thrown
    }

    @Test
    void require_non_null_should_throw_illegal_argument_exception_when_object_is_null() {
        thenThrownBy(() -> Safe.requireNonNull(null, "paramName"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("paramName must not be null");
    }

    // --- requireFullyNonNull (Array) ---

    @Test
    void require_fully_non_null_array_should_pass_when_all_elements_are_non_null() {
        Object[] array = new Object[]{"one", "two", 3};

        Safe.requireFullyNonNull(array, "arrayParam");

        then(array).hasSize(3);
    }

    @Test
    void require_fully_non_null_array_should_throw_when_array_itself_is_null() {
        Object[] array = null;

        thenThrownBy(() -> Safe.requireFullyNonNull(array, "arrayParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("arrayParam must not be null");
    }

    @Test
    void require_fully_non_null_array_should_throw_when_an_element_is_null() {
        Object[] array = new Object[]{"one", null, "three"};

        thenThrownBy(() -> Safe.requireFullyNonNull(array, "arrayParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("arrayParam must not be null");
    }

    // --- requireFullyNonNull (List) ---

    @Test
    void require_fully_non_null_list_should_pass_when_all_elements_are_non_null() {
        List<String> list = List.of("one", "two");

        Safe.requireFullyNonNull(list, "listParam");

        then(list).hasSize(2);
    }

    @Test
    void require_fully_non_null_list_should_throw_when_list_itself_is_null() {
        List<String> list = null;

        thenThrownBy(() -> Safe.requireFullyNonNull(list, "listParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("listParam must not be null");
    }

    @Test
    void require_fully_non_null_list_should_throw_when_an_element_is_null() {
        List<String> list = Arrays.asList("one", null, "three");

        thenThrownBy(() -> Safe.requireFullyNonNull(list, "listParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("listParam must not be null");
    }

    // --- requireNonEmpty ---

    @Test
    void require_non_empty_should_pass_when_string_has_content() {
        Safe.requireNonEmpty("valid", "strParam");

        then(true).isTrue();
    }

    @Test
    void require_non_empty_should_throw_when_string_is_null() {
        thenThrownBy(() -> Safe.requireNonEmpty(null, "strParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strParam must be non empty");
    }

    @Test
    void require_non_empty_should_throw_when_string_is_empty() {
        thenThrownBy(() -> Safe.requireNonEmpty("", "strParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strParam must be non empty");
    }

    // --- requireNonBlank ---

    @Test
    void require_non_blank_should_pass_when_string_has_non_whitespace_content() {
        Safe.requireNonBlank("valid", "strParam");

        then(true).isTrue();
    }

    @Test
    void require_non_blank_should_throw_when_string_is_null() {
        thenThrownBy(() -> Safe.requireNonBlank(null, "strParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strParam must be non blank");
    }

    @Test
    void require_non_blank_should_throw_when_string_is_empty() {
        thenThrownBy(() -> Safe.requireNonBlank("", "strParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strParam must be non blank");
    }

    @Test
    void require_non_blank_should_throw_when_string_is_whitespace_only() {
        thenThrownBy(() -> Safe.requireNonBlank("   ", "strParam"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strParam must be non blank");
    }
}