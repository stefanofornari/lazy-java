# LazyJava

LazyJava is a Java annotation processor and Maven plugin that generates factory methods and weaves runtime parameter validation into methods and constructors using ByteBuddy.

## Modules

| Module | Purpose |
|---|---|
| `lazyjava-annotation` | Annotations (`@£` / `@LClass`, `@NonNull`, `@NonBlank`, `@NonEmpty`) and the annotation processor that generates package-level `£` interfaces with static factory methods. |
| `lazyjava-core` | Core runtime utilities for LazyJava-based code, including the `Safe` default-method interface for null/blank/empty guards and conditional execution, and `FileLogHandler`, a JUL file handler configured entirely from `logging.properties`. |
| `lazyjava-maven-plugin` | Maven plugin that instruments compiled classes at build time so runtime validation is enforced without manual agent setup. |
| `lazyjava-examples` | Sample project demonstrating end-to-end usage. |

## Documentation

- [Getting started](getting-started.md) — add dependencies, configure the Maven plugin, generate factories, and enable runtime validation.
- [Requirements and compatibility](requirements.md) — Java/Maven requirements and module-specific toolchain notes.
- [File log handler](logging.md) — configure a rolling, size-capped log file entirely from `logging.properties`, with the system folders as tokens.
- [Modules](modules.md) — module responsibilities and current `lazyjava-core` capabilities.

## Source

- Repository: <https://github.com/stefanofornari/lazy-java>
- License: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
