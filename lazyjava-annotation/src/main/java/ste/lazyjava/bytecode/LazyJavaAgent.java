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