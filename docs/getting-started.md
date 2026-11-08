# Getting Started

This guide shows how to use LazyJava’s annotation processor and Maven plugin in your project.

## 1. Add dependencies

Add the annotation processor dependency:

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>lazyjava-annotation</artifactId>
    <version>1.0.0</version>
</dependency>
```

> Use the version appropriate for your project/release. Check the repository and published artifacts for the latest available versions.

## 2. Register the Maven plugin

Wire the `lazyjava-maven-plugin` into your build so validation is woven after compilation:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.stefanofornari</groupId>
            <artifactId>lazyjava-maven-plugin</artifactId>
            <version>0.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>weave</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

The plugin runs during `process-classes` and instruments your compiled classes automatically. No additional runtime agent setup is required.

## 3. Use the annotations

Annotate classes with `@£` or `@LClass` to generate static factory methods in a package-level `£` interface:

```java
package com.example;

import ste.lazyjava.annotation.£;

@£
public class Person {
    private String name;
    private int age;

    public Person() {
        this("Unknown", 0);
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

Import the generated factory interface and use it directly:

```java
import static com.example.£.*;

Person p = Person();
Person p2 = Person("John", 30);
```

## 4. Validate parameters at runtime

Use `@NonNull`, `@NonBlank`, and `@NonEmpty` on method or constructor parameters. The Maven plugin injects ByteBuddy advice so violations throw `IllegalArgumentException` automatically:

```java
@£
public class UserService {
    public void register(@NonNull String name, @NonBlank String email, @NonEmpty String username) {
        // method body
    }
}
```

| Annotation | Target | Fails when |
|------------|--------|------------|
| `@NonNull` | parameter | value is `null` |
| `@NonBlank` | parameter | value is `null` or blank |
| `@NonEmpty` | parameter | value is `null` or empty |

## Next

- Review [Requirements and compatibility](requirements.md).
- See [Modules](modules.md) for module responsibilities.
