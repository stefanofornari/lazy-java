# LazyJava

LazyJava is a Java annotation processor and Maven plugin that generates factory methods and weaves runtime parameter validation into methods and constructors using ByteBuddy.

## Modules

- `lazyjava-annotation` — annotations (`@£` / `@LClass`, `@NonNull`, `@NonBlank`, `@NonEmpty`) and the annotation processor that generates package-level `£` interfaces with static factory methods.
- `lazyjava-core` — core runtime utilities for LazyJava-based code: the `Safe` default-method interface and `ste.lazyjava.logging.FileLogHandler`, a JUL file handler configurable entirely from `logging.properties` (system folders and `${...}` tokens in the pattern; always size-capped).
- `lazyjava-maven-plugin` — Maven plugin that instruments compiled classes at build time so runtime validation is enforced without manual agent setup.
- `lazyjava-examples` — sample project demonstrating end-to-end usage.

## Documentation

Detailed usage and compatibility guidance is published in the GitHub Pages site:

- [Getting started](https://stefanofornari.github.io/lazy-java/getting-started.html)
- [Requirements and compatibility](https://stefanofornari.github.io/lazy-java/requirements.html)
- [Modules](https://stefanofornari.github.io/lazy-java/modules.html)
- [File log handler](https://stefanofornari.github.io/lazy-java/logging.html)

## Quick start

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>lazyjava-annotation</artifactId>
    <version>1.0.0</version>
</dependency>
```

Register the Maven plugin and see the full examples in [Getting started](https://stefanofornari.github.io/lazy-java/getting-started.html).

## License

Apache License 2.0
