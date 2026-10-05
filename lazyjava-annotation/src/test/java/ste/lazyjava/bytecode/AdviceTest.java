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

import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class AdviceTest {

    @SuppressWarnings("unused")
    private static class SampleTarget {

        public SampleTarget(@NonNull String name, @NonBlank String email, @NonEmpty String username) {}

        public void nonNullParam(@NonNull String val) {}

        public void nonBlankParam(@NonBlank String val) {}

        public void nonEmptyParam(@NonEmpty String val) {}

        public void multipleParams(@NonNull String a, @NonBlank String b, @NonEmpty String c) {}
    }

    private Method getMethod(String name, Class<?>... paramTypes) throws NoSuchMethodException {
        return SampleTarget.class.getDeclaredMethod(name, paramTypes);
    }

    private Constructor<SampleTarget> getConstructor() throws NoSuchMethodException {
        return SampleTarget.class.getDeclaredConstructor(String.class, String.class, String.class);
    }

    // --- NonNullMethodAdvice Tests ---

    @Test
    void method_nonNull_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonNullParam", String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must not be null");
    }

    @Test
    void method_nonNull_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonNullParam", String.class);

        thenCode(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"hello"}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_nonBlank_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non blank");
    }

    @Test
    void method_nonBlank_shouldThrow_whenBlank() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"   "}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non blank");
    }

    @Test
    void method_nonBlank_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonBlankParam", String.class);

        thenCode(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"valid"}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_nonEmpty_shouldThrow_whenNull() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{null}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non empty");
    }

    @Test
    void method_nonEmpty_shouldThrow_whenEmpty() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("val must be non empty");
    }

    @Test
    void method_nonEmpty_shouldPass_whenValid() throws Exception {
        Method method = getMethod("nonEmptyParam", String.class);

        thenCode(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"a"}))
            .doesNotThrowAnyException();
    }

    @Test
    void method_multipleParams_shouldFail_onFirstError() throws Exception {
        Method method = getMethod("multipleParams", String.class, String.class, String.class);

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{null, "valid", "valid"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("a must not be null");

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"valid", "  ", "valid"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("b must be non blank");

        thenThrownBy(() -> NonNullMethodAdvice.onMethodEnter(method, new Object[]{"valid", "valid", ""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("c must be non empty");
    }

    // --- NonNullConstructorAdvice Tests ---

    @Test
    void constructor_shouldThrow_whenNonNullParamIsNull() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> NonNullConstructorAdvice.onMethodEnter(constructor, new Object[]{null, "email@test.com", "user123"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("name must not be null");
    }

    @Test
    void constructor_shouldThrow_whenNonBlankParamIsBlank() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> NonNullConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "   ", "user123"}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("email must be non blank");
    }

    @Test
    void constructor_shouldThrow_whenNonEmptyParamIsEmpty() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenThrownBy(() -> NonNullConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "email@test.com", ""}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("username must be non empty");
    }

    @Test
    void constructor_shouldPass_whenAllParametersAreValid() throws Exception {
        Constructor<SampleTarget> constructor = getConstructor();

        thenCode(() -> NonNullConstructorAdvice.onMethodEnter(constructor, new Object[]{"Alice", "email@test.com", "user123"}))
            .doesNotThrowAnyException();
    }
}