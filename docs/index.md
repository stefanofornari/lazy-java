# LazyJava

LazyJava is a collection of small Java utilities that make tedious things easier: less boilerplate, safer code.

* **Argument validation by annotation**: `@NonNull`, `@NonBlank` and `@NonEmpty` replace the usual `if (x == null) throw ...`; the checks are woven into the compiled classes at build time.
* **Class-level facilities with `@£`**: annotate a class to get easier ways to work with it. Today that is a static factory method for each public constructor; other class-level facilities will follow.
* **`Safe`**: default methods to write safer code that does something only if an expression is, or is not, null, and to require non-null, non-empty or non-blank values.
* **`FileLogHandler`**: a `java.util.logging` file handler configured entirely from `logging.properties`, to put the log in the places the system defines for application data (with `${dataDir}` and friends), always size-capped.
* ... and more to come: the project grows by adding small utilities that remove a recurring chore.

The annotation processor and the Maven plugin are the machinery behind the first two; the runtime helpers (`Safe`, `FileLogHandler`) live in `lazyjava-core`.

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
