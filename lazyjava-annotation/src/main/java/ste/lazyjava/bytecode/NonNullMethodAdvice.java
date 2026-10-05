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

import net.bytebuddy.asm.Advice;
import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NonNullMethodAdvice {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
        System.out.println("onMethodEnter " + method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Object arg = args[i];
            Parameter param = parameters[i];
            String name = param.getName();

            System.out.println("param name " + name);

            if (param.isAnnotationPresent(NonNull.class) && arg == null) {
                throw new IllegalArgumentException(name + " must not be null");
            }

            if (param.isAnnotationPresent(NonBlank.class)) {
                System.out.println("nonBlank");
                if (arg == null || (arg instanceof String s && s.trim().isBlank())) {
                    throw new IllegalArgumentException(name + " must be non blank");
                }
            }

            if (param.isAnnotationPresent(NonEmpty.class)) {
                System.out.println("nonEmpty");
                if (arg == null || (arg instanceof String s && s.isEmpty())) {
                    throw new IllegalArgumentException(name + " must be non empty");
                }
            }
        }
    }
}