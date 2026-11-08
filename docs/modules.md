# Modules

LazyJava is a multi-module Maven project.

## `lazyjava-annotation`

Provides annotations and the annotation processor:

- `@£` / `@LClass` — mark classes for generated package-level factory interfaces.
- `@NonNull`, `@NonBlank`, `@NonEmpty` — parameter validation annotations used with build-time weaving.

## `lazyjava-core`

Core runtime utilities for LazyJava-based code.

Current implemented API: `ste.lazyjava.safe.Safe` (default-method interface).

| Capability | Methods |
|---|---|
| Conditional execution | `ifNotNull`, `ifNull` (2-arg/3-arg), `safe` |
| Null validation | `requireNonNull`, `requireFullyNonNull` for arrays and lists |
| String validation | `requireNonEmpty`, `requireNonBlank` |

Validation failures throw `IllegalArgumentException` with parameter-name-based messages.

## `lazyjava-maven-plugin`

Maven plugin that instruments compiled classes during the build (ByteBuddy-based weaving workflow), so runtime validation annotations can be enforced without manual Java agent setup.

## `lazyjava-examples`

Sample project demonstrating end-to-end usage of annotations, generated factories, and validation.
