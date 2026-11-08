# Modules

LazyJava is a multi-module Maven project.

## `lazyjava-annotation`

Provides annotations and the annotation processor:

- `@£` / `@LClass` — mark classes for generated package-level factory interfaces.
- `@NonNull`, `@NonBlank`, `@NonEmpty` — parameter validation annotations used with build-time weaving.

## `lazyjava-core`

Core runtime utilities for LazyJava-based code.

Current implemented API:

- `ste.lazyjava.safe.Safe` (default-method interface)
- `ste.lazyjava.logging.FileLogHandler` (a `java.util.logging` file handler configured entirely from `logging.properties`; see [File log handler](logging.md))

| Capability | Methods |
|---|---|
| Conditional execution | `ifNotNull`, `ifNull` (2-arg/3-arg), `safe` |
| Null validation | `requireNonNull`, `requireFullyNonNull` for arrays and lists |
| String validation | `requireNonEmpty`, `requireNonBlank` |

Validation failures throw `IllegalArgumentException` with parameter-name-based messages.

`FileLogHandler` expands `${dataDir}`, `${cacheDir}`, `${configDir}`, `${application}`, `${env.NAME}` and system properties in the log file pattern, creates the log folder and keeps the log size bounded by default. Details, settings and an example are in [File log handler](logging.md).

## `lazyjava-maven-plugin`

Maven plugin that instruments compiled classes during the build (ByteBuddy-based weaving workflow), so runtime validation annotations can be enforced without manual Java agent setup.

## `lazyjava-examples`

Sample project demonstrating end-to-end usage of annotations, generated factories, and validation.
