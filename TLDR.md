# LazyJava

> LazyJava (Apache-2.0, author Stefano Fornari, repository https://github.com/stefanofornari/lazy-java, group id `com.github.stefanofornari`) is a collection of small Java utilities that make tedious things easier: less boilerplate, safer code. Today: (1) argument validation by annotation (`@NonNull`, `@NonBlank`, `@NonEmpty`) instead of hand-written `if (x == null) throw ...`, woven in at build time by a Maven plugin (ByteBuddy); (2) the class annotation `@£`, which adds easier ways to work with a class (today a static factory per public constructor, via an annotation processor; more class-level facilities will follow); (3) `Safe`, default methods for writing safer code that acts only if an expression is or is not null; (4) `FileLogHandler`, a `java.util.logging` file handler configured entirely from `logging.properties` to put the log in system-defined folders. The collection grows by adding utilities that remove a recurring chore.

This file is a short summary for quick readers, LLMs and tools. Keep it in sync with the code; the code, tests and `docs/` are authoritative. Human documentation: https://stefanofornari.github.io/lazy-java/ (sources in `docs/`).

## Modules

| Module (artifactId) | What it is | Java |
|---|---|---|
| `lazyjava-annotation` | Annotations `@£`, `@LClass`, `@NonNull`, `@NonBlank`, `@NonEmpty`, the interface `LazyJava`, the annotation processor `ste.lazyjava.processor.£Processor` (registered with AutoService) and the ByteBuddy advice used for weaving | processor declares source 21 |
| `lazyjava-maven-plugin` | Maven plugin, goal `weave` (default phase `process-classes`) that instruments the compiled classes (and test classes, if any) | 21 |
| `lazyjava-core` | Runtime utilities: `ste.lazyjava.safe.Safe`, `ste.lazyjava.logging.FileLogHandler`. It also builds with the processor and the plugin | 11 (module overrides the parent's 21) |
| `lazyjava-examples` | Sample project: `User`, `Person`, `CollectionExample` and a test (`AllTest`); not published | 21 |

Repository requirements stated in the README: Java 21+, Maven 3.6+. The parent POM is `lazyjava-parent`. Releases go to Maven Central (central-publishing plugin, GPG signing).

## 1. Factory generation (`lazyjava-annotation`)

- Annotate a class with `@£` (alias `@LClass`; both `RetentionPolicy.SOURCE`, target TYPE).
- The processor generates, once per package, a public interface named `£` in that package, with one `public static` factory method per **public constructor** of each annotated class. The method has the simple name of the class and the same parameters, e.g. `Person Person(String name, int age)`.
- Use it with `import static com.example.£.*;` then `Person p = Person("John", 30);`.
- Generated sources go to `target/generated-sources/annotations`; `lazyjava-core/pom.xml` shows the build-helper configuration that adds them to the compilation, and the `annotationProcessors` entry `ste.lazyjava.processor.£Processor`.

## 2. Runtime parameter validation (`lazyjava-annotation` + `lazyjava-maven-plugin`)

- `@NonNull`, `@NonBlank`, `@NonEmpty` are `RetentionPolicy.RUNTIME`, target PARAMETER, on methods and constructors.
- Register the plugin with the `weave` goal. After compilation it injects ByteBuddy advice (`ValidatorMethodAdvice`, `ValidatorConstructorAdvice`); no Java agent is needed at runtime.
- A violated check throws `IllegalArgumentException`. `@NonNull`: value is null. `@NonBlank`: null or blank. `@NonEmpty`: null or empty (strings, collections, arrays).

```xml
<plugin>
  <groupId>com.github.stefanofornari</groupId>
  <artifactId>lazyjava-maven-plugin</artifactId>
  <version>…</version>
  <executions><execution><goals><goal>weave</goal></goals></execution></executions>
</plugin>
```

## 3. `ste.lazyjava.safe.Safe` (`lazyjava-core`)

An interface with only default methods: implement it (or call from a class that does) to use them. Checks throw `IllegalArgumentException` with a message `"<name> must not be null"`, `"<name> must be non empty"`, `"<name> must be non blank"`.

- `void ifNotNull(Object o, Runnable r)`: runs `r` if `o != null`.
- `void ifNull(Object o, Runnable doIfNull)`; `void ifNull(Object o, Runnable doIfNull, Runnable doIfNotNull)`.
- `<T> void safe(T o, Consumer<T> c)`: calls `c` with `o` if `o != null`.
- `void requireNonNull(Object o, String name)`.
- `void requireFullyNonNull(Object[] o, String name)` and `(List<?> o, String name)`: the container and every element must be non-null.
- `void requireNonEmpty(String s, String name)`, `void requireNonBlank(String s, String name)`.

## 4. `ste.lazyjava.logging.FileLogHandler` (`lazyjava-core`)

A subclass of `java.util.logging.FileHandler` configured **only** from `logging.properties` (read by JUL, e.g. `-Djava.util.logging.config.file=logging.properties`). It expands `${...}` tokens in the file pattern before the file is opened, creates the folder, and then behaves as a `FileHandler`. Loaded by the `LogManager` from the classpath (public no-arg constructor; module path not supported). Depends on `dev.dirs:directories` (directories-jvm) for system folders.

```properties
handlers = java.util.logging.ConsoleHandler, ste.lazyjava.logging.FileLogHandler
ste.lazyjava.logging.FileLogHandler.application = MyApp
ste.lazyjava.logging.FileLogHandler.pattern     = ${dataDir}/logs/${application}.%g.log
ste.lazyjava.logging.FileLogHandler.limit       = 1000000
ste.lazyjava.logging.FileLogHandler.count       = 3
```

- Settings (all under `ste.lazyjava.logging.FileLogHandler.`; the plain `FileHandler` settings are ignored): `pattern` (default `${dataDir}/logs/${application}.%g.log`), `limit` (1 000 000 bytes), `count` (3), `append` (true), `formatter` (default `SimpleFormatter`, not XML), `level`, `filter`, `encoding`, `application`, `qualifier`, `organization`.
- The size is always bounded: missing, invalid or too small `limit`/`count` fall back to the defaults.
- Tokens: `${dataDir}`, `${cacheDir}`, `${configDir}` (system folders of the application; `application` is required), `${application}`, `${env.NAME}` (environment variable), any other `${name}` is a system property. Values are made safe for a pattern (`\` to `/`, `%` escaped); `%g`, `%u`, `%h`, `%t`, `%%` keep their JDK meaning.
- A token with no value (unknown, unset variable, missing `application`, unterminated) never throws: it is left as is in the pattern and a warning is reported once, as the first record of the file and on `System.err`.
- If the file cannot be created the constructor throws `IOException`; the `LogManager` reports it and the other handlers keep working. No fallback folder.
- Only the logger levels are left to code. Full page: `docs/logging.md`.

## Conventions

- Tests: JUnit 5 + AssertJ in BDD style (`then(...)`), test names in snake_case describing behaviour; new behaviour starts with a failing test.
- Source headers: Apache-2.0 notice; `£` is a legal Java identifier used on purpose in names (`ste.lazyjava.processor.£Processor`, generated `£` interfaces).
- Work is organised as user stories in the Uzzo knowledge base: `lazyjava-core/uzz/` (`stories/<domain>/US-nnnnnn/`, `stories/index.md`, `stories/scope.md` and `stories/<domain>/scope.md`, project docs in `project/`, skills in `skills/`).

## Where to look

- `lazyjava-core/src/main/java/ste/lazyjava/safe/Safe.java`, `.../logging/FileLogHandler.java` and their tests.
- `lazyjava-annotation/src/main/java/ste/lazyjava/processor/£Processor.java` and `.../bytecode/`.
- `lazyjava-maven-plugin/src/main/java/ste/lazyjava/maven/LazyJavaMojo.java`.
- `lazyjava-examples/` for end-to-end usage.
- `docs/` (GitHub Pages): `getting-started.md`, `requirements.md`, `modules.md`, `logging.md`.
