# LazyJava

LazyJava is a Java annotation processor and Maven plugin that generates factory methods and weaves runtime parameter validation into methods and constructors using ByteBuddy.

## Modules

- `lazyjava-annotation` — annotations (`@£` / `@LClass`, `@NonNull`, `@NonBlank`, `@NonEmpty`) and the annotation processor that generates package-level `£` interfaces with static factory methods.
- `lazyjava-core` — tests and usage examples for the annotation processor.
- `lazyjava-maven-plugin` — Maven plugin that instruments compiled classes at build time so runtime validation is enforced without manual agent setup.
- `lazyjava-examples` — sample project demonstrating end-to-end usage.

## Usage

### 1. Add dependencies

Add the annotation processor and Maven plugin to your project:

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>lazyjava-annotation</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Register the Maven plugin

Wire the `lazyjava-maven-plugin` into your build so validation is woven after compilation:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.stefanofornari</groupId>
            <artifactId>lazyjava-maven-plugin</artifactId>
            <version>0.0.0</version>
        </plugin>
    </plugins>
</build>
```

The plugin runs during `process-classes` and instruments your compiled classes automatically. No additional runtime agent setup is required.

### 3. Use the annotations

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

### 4. Validate parameters at runtime

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

### 5. Requirements

- Java 21+
- Maven 3.6+

## License

Apache License 2.0
