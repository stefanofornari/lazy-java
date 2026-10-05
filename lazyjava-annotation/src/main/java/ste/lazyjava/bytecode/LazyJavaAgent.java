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

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class LazyJavaAgent {

    public static void install(String args, Instrumentation inst) {
        new AgentBuilder.Default()
            .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
            .ignore(nameStartsWith("net.bytebuddy.")
                .or(nameStartsWith("java."))
                .or(nameStartsWith("javax."))
                .or(nameStartsWith("org.junit."))
                .or(nameStartsWith("org.assertj."))
                .or(nameStartsWith("ste.lazyjava.bytecode."))
                .or(nameEndsWith("Test"))) // Exclude test classes!
            .type(nameStartsWith("ste.lazyjava."))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                builder
                    .visit(Advice.to(NonNullMethodAdvice.class)
                        .on(isMethod().and(not(isAbstract()))))
                    .visit(Advice.to(NonNullConstructorAdvice.class)
                        .on(isConstructor()))
            )
            .installOn(inst);
    }
}