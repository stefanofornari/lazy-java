# LazyJava

LazyJava is a collection of small Java utilities that make tedious things easier: less boilerplate, safer code.

* **Argument validation by annotation**: `@NonNull`, `@NonBlank` and `@NonEmpty` replace the usual `if (x == null) throw ...`; the checks are woven into the compiled classes at build time.
* **Class-level facilities with `@£`**: annotate a class to get easier ways to work with it. Today that is a static factory method for each public constructor; other class-level facilities will follow.
* **`Safe`**: default methods to write safer code that does something only if an expression is, or is not, null, and to require non-null, non-empty or non-blank values.
* ... and more to come: the project grows by adding small utilities that remove a recurring chore.

The annotation processor and the Maven plugin are the machinery behind the first two; the runtime helpers live in `lazyjava-core`.

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
- [`TLDR.md`](TLDR.md) — a short summary of the whole project, for quick readers, LLMs and tools

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
