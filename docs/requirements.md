# Requirements and Compatibility

## Project-level requirements

The repository `README.md` currently states:

- Java 21+
- Maven 3.6+

The parent POM also configures Java 21 for the multi-module build (`maven.compiler.source/target/release` = 21).

## Module-specific toolchain notes

`lazyjava-core` currently compiles with Java 11 source/target in `lazyjava-core/pom.xml`.

This means the overall LazyJava project baseline and individual module compiler settings may not express the same compatibility boundary. Until module-specific compatibility is explicitly documented and validated, consumers should treat the repository-level requirement as the primary guidance and verify module compatibility for their target environment.

## Related documentation

- [Getting started](getting-started.md)
- [Modules](modules.md)
