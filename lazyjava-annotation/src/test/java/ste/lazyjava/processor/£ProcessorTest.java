package ste.lazyjava.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;

import static com.google.testing.compile.Compiler.javac;
import static org.assertj.core.api.BDDAssertions.then;

class £ProcessorTest {

    private String getGeneratedSource(Compilation compilation, String className) throws IOException {
        JavaFileObject file = compilation
            .generatedFile(StandardLocation.SOURCE_OUTPUT, className.replace('.', '/') + ".java")
            .orElseThrow(() -> new AssertionError("Expected generated file not found: " + className));
        return file.getCharContent(true).toString();
    }

    @Test
    void should_generate_interface_with_factory_methods_for_annotated_class() throws Exception {
        JavaFileObject sampleSource = JavaFileObjects.forSourceString(
            "com.example.Person",
            """
            package com.example;

            import ste.lazyjava.annotation.£;

            @£
            public class Person {
                public Person() {}
                public Person(String name, int age) {}
            }
            """
        );

        Compilation compilation = javac()
            .withProcessors(new £Processor())
            .compile(sampleSource);

        then(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        String generatedContent = getGeneratedSource(compilation, "com.example.£");

        then(generatedContent)
            .contains("package com.example;")
            .contains("public interface £ {")
            .contains("public static Person Person() {")
            .contains("return new Person();")
            .contains("public static Person Person(java.lang.String name, int age) {")
            .contains("return new Person(name, age);");
    }

    @Test
    void should_ignore_non_public_constructors() throws Exception {
        JavaFileObject sampleSource = JavaFileObjects.forSourceString(
            "com.example.Singleton",
            """
            package com.example;

            import ste.lazyjava.annotation.£;

            @£
            public class Singleton {
                private Singleton() {}
            }
            """
        );

        Compilation compilation = javac()
            .withProcessors(new £Processor())
            .compile(sampleSource);

        then(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        String generatedContent = getGeneratedSource(compilation, "com.example.£");

        then(generatedContent)
            .contains("package com.example;")
            .contains("public interface £ {")
            .doesNotContain("Singleton(");
    }

    @Test
    void should_handle_nested_and_inner_classes() throws Exception {
        JavaFileObject sampleSource = JavaFileObjects.forSourceString(
            "com.example.Outer",
            """
            package com.example;

            import ste.lazyjava.annotation.£;

            public class Outer {
                @£
                public static class Inner {
                    public Inner(String data) {}
                }
            }
            """
        );

        Compilation compilation = javac()
            .withProcessors(new £Processor())
            .compile(sampleSource);

        then(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        String generatedContent = getGeneratedSource(compilation, "com.example.£");

        then(generatedContent)
            .contains("package com.example;")
            .contains("public interface £ {")
            .contains("public static Outer.Inner Inner(java.lang.String data) {")
            .contains("return new Outer.Inner(data);");
    }
}