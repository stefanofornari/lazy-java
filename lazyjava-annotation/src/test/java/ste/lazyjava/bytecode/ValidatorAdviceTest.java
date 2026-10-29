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
package ste.lazyjava.bytecode;

import org.junit.jupiter.api.Test;
import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class ValidatorAdviceTest {

    @SuppressWarnings("unused")
    private static class SampleTarget {

        public SampleTarget(@NonNull String name, @NonBlank String email, @NonEmpty String username) {}

        public SampleTarget(@NonNull String name, @NonEmpty List<String> items, @NonEmpty String[] tags, @NonEmpty int[] values) {}

        public void nonNullParam(@NonNull String val) {}

        public void nonBlankParam(@NonBlank String val) {}

        public void nonEmptyParam(@NonEmpty String val) {}

        public void nonEmptyCollectionParam(@NonEmpty List<String> items) {}

        public void nonEmptyArrayParam(@NonEmpty String[] items) {}

        public void nonEmptyPrimitiveArrayParam(@NonEmpty int[] items) {}

        public void multipleParams(@NonNull String a, @NonBlank String b, @NonEmpty String c) {}
    }

    private Method getMethod(String name, Class<?>... paramTypes) throws NoSuchMethodException {
        return SampleTarget.class.getDeclaredMethod(name, paramTypes);
    }

    private Constructor<SampleTarget> getConstructor() throws NoSuchMethodException {
        return SampleTarget.class.getDeclaredConstructor(String.class, String.class, String.class);
    }

    private Constructor<SampleTarget> getCollectionArrayConstructor() throws NoSuchMethodException {
        return SampleTarget.class.getDeclaredConstructor(String.class, List.class, String[].class, int[].class);
    }

    // --- NonNullMethodAdvice Tests ---

    @Test
    void method_nonNull_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonNullParam", String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must not be null");
    }

    @Test
    void method_nonNull_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonNullParam", String.class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"hello"}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_nonBlank_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non blank");
    }

    @Test
    void method_nonBlank_shouldThrow_whenBlank() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"   "}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non blank");
    }

    @Test
    void method_nonBlank_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"valid"}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_nonEmpty_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non empty");
    }

    @Test
    void method_nonEmpty_shouldThrow_whenEmpty() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non empty");
    }

    @Test
    void method_nonEmpty_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"a"}))
            .doesNotThrowAnyException();
    }

    // --- NonEmpty with Collections Tests ---

    @Test
    void method_nonEmptyCollection_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonEmptyCollectionParam", List.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyCollection_shouldThrow_whenEmpty() throws Exception {
        Method method = getMethod("nonEmptyCollectionParam", List.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{List.of()}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyCollection_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonEmptyCollectionParam", List.class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{List.of("a")}))
            .doesNotThrowAnyException();
    }

    // --- NonEmpty with Arrays Tests ---

    @Test
    void method_nonEmptyArray_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonEmptyArrayParam", String[].class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyArray_shouldThrow_whenEmpty() throws Exception {
        Method method = getMethod("nonEmptyArrayParam", String[].class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{new String[0]}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyArray_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonEmptyArrayParam", String[].class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{new String[]{"a"}}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_nonEmptyPrimitiveArray_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonEmptyPrimitiveArrayParam", int[].class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyPrimitiveArray_shouldThrow_whenEmpty() throws Exception {
        Method method = getMethod("nonEmptyPrimitiveArrayParam", int[].class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{new int[0]}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void method_nonEmptyPrimitiveArray_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonEmptyPrimitiveArrayParam", int[].class);

        thenCode(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{new int[]{1}}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_multipleParams_shouldFail_onFirstError() throws Exception {
        Method method = getMethod("multipleParams", String.class, String.class, String.class);

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{null, "valid", "valid"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("a must not be null");

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"valid", "  ", "valid"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("b must be non blank");

        thenThrownBy(() -> ValidatorMethodAdvice.onMethodEnter(method, new Object[]{"valid", "valid", ""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("c must be non empty");
    }

    // --- NonNullConstructorAdvice Tests ---

    @Test
    void constructor_shouldThrow_whenNonNullParamIsNull() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{null, "email@test.com", "user123"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("name must not be null");
    }

    @Test
    void constructor_shouldThrow_whenNonBlankParamIsBlank() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "   ", "user123"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("email must be non blank");
    }

    @Test
    void constructor_shouldThrow_whenNonEmptyParamIsEmpty() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "email@test.com", ""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("username must be non empty");
    }

    @Test
    void constructor_shouldPass_whenAllParametersAreValid() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenCode(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "email@test.com", "user123"}))
            .doesNotThrowAnyException();
    }

    // --- NonNullConstructorAdvice with Collections and Arrays Tests ---

    @Test
    void constructor_withCollection_shouldThrow_whenNull() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", null, new String[]{"tag"}, new int[]{1}}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void constructor_withCollection_shouldThrow_whenEmpty() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of(), new String[]{"tag"}, new int[]{1}}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("items must be non empty");
    }

    @Test
    void constructor_withArray_shouldThrow_whenNull() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of("item"), null, new int[]{1}}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("tags must be non empty");
    }

    @Test
    void constructor_withArray_shouldThrow_whenEmpty() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of("item"), new String[0], new int[]{1}}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("tags must be non empty");
    }

    @Test
    void constructor_withPrimitiveArray_shouldThrow_whenNull() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of("item"), new String[]{"tag"}, null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("values must be non empty");
    }

    @Test
    void constructor_withPrimitiveArray_shouldThrow_whenEmpty() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenThrownBy(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of("item"), new String[]{"tag"}, new int[0]}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("values must be non empty");
    }

    @Test
    void constructor_withCollectionAndArrays_shouldPass_whenValid() throws Exception {
        Constructor<SampleTarget> constructor = getCollectionArrayConstructor();

        thenCode(() -> ValidatorConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", List.of("item"), new String[]{"tag"}, new int[]{1}}))
            .doesNotThrowAnyException();
    }
}