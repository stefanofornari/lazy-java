# Project Name: LazyJava Core
**Version:** 0.0.2-SNAPSHOT

## Overall Description

LazyJava Core is the `lazyjava-core` Maven module of the LazyJava project. It provides core runtime utilities and serves as the module that compiles with the LazyJava annotation processor and build-time weaving setup, exercising LazyJava’s generated-factory and validation model (as documented in `README.md` and configured in `lazyjava-core/pom.xml`).

Functional domains:
- **Safe utility API (`ste.lazyjava.safe.Safe`)**: Default-method interface providing null/blank/empty guard helpers and conditional execution (`ifNull`/`ifNotNull`/`safe`) with `IllegalArgumentException`-based requirement checks (including fully-non-null checks for arrays/lists).
- **Annotation-processing integration (module build)**: Maven compiler configuration runs `ste.lazyjava.processor.£Processor` (from `lazyjava-annotation`) and includes generated annotation sources during build.
- **Build-time weaving integration (module build)**: Maven build wires `lazyjava-maven-plugin` (goal `weave`, as used by the module/parent configuration) for ByteBuddy-based instrumentation in the LazyJava workflow.
- **Verification/tests**: JUnit 5 + AssertJ tests for module behavior (currently `SafeTest`).

## In-Scope Capabilities

* **Safe utility API**:
  - [x] `Safe` interface with `ifNotNull`, `ifNull` (2/3-arg), and `safe` consumer helpers.
  - [x] `requireNonNull`, `requireFullyNonNull` (array/list), `requireNonEmpty`, and `requireNonBlank` throwing `IllegalArgumentException` with descriptive messages.
* **Build & integration (current module state)**:
  - [x] Java/Maven module `com.github.stefanofornari:lazyjava-core:0.0.2-SNAPSHOT` (parent `lazyjava-parent:0.0.2`).
  - [x] Annotation processor execution for `£` factory generation (`ste.lazyjava.processor.£Processor`) and generated-sources handling.
  - [x] `lazyjava-maven-plugin` configured in the module build (LazyJava weaving workflow).
  - [x] Module dependencies used by current implementation/tests (e.g., `lazyjava-annotation`, `commons-lang3`, `lambda-loop`; JUnit 5/AssertJ for tests).
* **Documentation alignment**:
  - [x] Project metadata in `pom.xml` and repository `README.md` treated as authoritative/trustful sources for current capabilities and module context.

## Out-of-Scope / Future Enhancements

* None specified for this scope baseline (only current, implemented/module-configured capabilities are listed in scope).

## Technical Context & Infrastructure

* **Development Toolchain**: Java 11 (`maven-compiler-plugin` source/target 11) + Maven; JUnit 5, AssertJ; LazyJava annotation processor; `lazyjava-maven-plugin` (weave).
* **Target Systems/Platforms**: JVM library/module (Maven-built). (Repository-level LazyJava context, per `README.md`: Java 21+/Maven 3.6+ for overall usage.)
* **Project Reference Docs**:
  - Project Overview: `lazyjava-core/uzz/project/overview.md`
  - Coding Standard: `lazyjava-core/uzz/project/coding_standard.md`
  - Development Framework: `lazyjava-core/uzz/project/development_framework.md`

### Keywords
- scope
- requirements
- boundaries
- in-scope
- lazyjava-core
- safe
- annotation-processor
- maven-plugin
