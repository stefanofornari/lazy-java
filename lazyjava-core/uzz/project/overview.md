# Project Overview

## Identity

- **Name**: LazyJava Core
- **Version**: 0.0.2-SNAPSHOT (`lazyjava-core/pom.xml`; parent `lazyjava-parent:0.0.2`)
- **Summary**: `lazyjava-core` is a Maven module in the LazyJava project. It provides runtime utility code (currently the `ste.lazyjava.safe.Safe` default-method interface for null/blank/empty guards and conditional execution) and is configured to participate in LazyJava’s compile-time factory generation and build-time weaving workflow via the annotation processor and `lazyjava-maven-plugin`. Repository `README.md` and module POMs are treated as authoritative sources for module context.

## Team & Roles

| Person | Role | Responsibilities |
|--------|------|------------------|
| Stefano Fornari | Author/Maintainer (per project POM metadata) | Module and project maintenance |

## Timeline

- **Start**: Not specified in repository docs read for this scope pass.
- **Milestones**:
  - Current module baseline — 0.0.2-SNAPSHOT

## Repositories

- [lazy-java](https://github.com/stefanofornari/lazy-java) — Source repository containing `lazyjava-core` and related LazyJava modules.

## Architecture

- **Module role**: Core module depending on `lazyjava-annotation`; compiled with `ste.lazyjava.processor.£Processor` and `lazyjava-maven-plugin` (weave) configured in the build.
- **Main code**: `src/main/java/ste/lazyjava/safe/Safe.java` (default guard/conditional helpers).
- **Tests**: JUnit 5 + AssertJ (`src/test/java/ste/lazyjava/safe/SafeTest.java`).
- **Generated sources**: Build helper adds `target/generated-sources/annotations` (annotation processor output) to compilation.

### Keywords
- project
- overview
- lazyjava-core
- safe
- team
- repositories
- architecture
