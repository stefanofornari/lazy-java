# Development Framework

## Purpose

How `lazyjava-core` is built, tested, and integrated into the LazyJava build workflow.

## Technology Stack

- **Language / Runtime**: Java 11 (module compiler `source`/`target` 11 per `lazyjava-core/pom.xml`); repository-level LazyJava usage context states Java 21+ (`README.md`).
- **Frameworks / Libraries**: `lazyjava-annotation` (annotation API/processor integration), Apache Commons Lang3, lambda-loop; LazyJava Maven plugin (weave).
- **Build Tool**: Maven (multi-module; parent `lazyjava-parent`).
- **Test Tools**: JUnit 5 (`junit-jupiter`), AssertJ.

## Development Workflow

- Keep module behavior covered by unit tests (JUnit 5 + AssertJ).
- Annotation-processor-generated sources are produced/added during build (`generate-sources`/`generate-test-sources` via build-helper).
- ByteBuddy-based LazyJava weaving is applied via `lazyjava-maven-plugin` in the module build.

## Build and Execution

- **Build**: `mvn clean install` (from repo/module context)
- **Test**: `mvn test`
- **Run**: N/A (library module; consumed via Maven/build output)

### Keywords
- development-framework
- toolchain
- build
- testing
- workflow
- lazyjava-core
