# Project Name: LazyJava Core — Logging
**Version:** 0.1.0

## Overall Description
`ste.lazyjava.logging.FileLogHandler` is a `java.util.logging` file handler that can be configured
entirely from the standard `logging.properties` (read by JUL itself, e.g. with
`-Djava.util.logging.config.file`), including the folder of the log file. The JDK `FileHandler` opens
its file in the constructor and only understands `%h`, `%t`, `%u` and `%g` in its pattern, so the
folder of an application cannot be expressed in the properties. `FileLogHandler` expands `${...}`
tokens in the pattern *before* the file is opened, creates the folder, and then behaves as a
`FileHandler` (rotation, `%g`, level, filter, encoding). Only the logger levels remain a matter of code.

Decisions taken while scoping:
* **Name and package**: `ste.lazyjava.logging.FileLogHandler` (it replaces the working name `ConfigurableFileHandler` of the first spike).
* **Own namespace**: settings are read from the `LogManager` under the class name (`ste.lazyjava.logging.FileLogHandler.<name>`), so it can coexist with a plain `FileHandler`.
* **Bounded by default**: the log never grows indefinitely, even if `limit` and `count` are not set (1 000 000 bytes, 3 files).
* **System folders**: found with [directories-jvm](https://codeberg.org/dirs/directories-jvm) (`dev.dirs:directories`), the library for any system folder in this project.
* **Application name is required** when a system-folder token is used: no generic folder such as `app` is ever created.
* **On failure the handler throws** (`IOException`); JUL reports it and the other handlers keep working. No fallback location, no silence.
* **Default formatter is `SimpleFormatter`** when none is configured (the JDK default would be XML), so the file is readable out of the box.

## In-Scope Capabilities
* **Configuration from `logging.properties`**:
  - [ ] `FileLogHandler` can be listed in `handlers` and is created by the `LogManager` through its public no-argument constructor
  - [ ] Settings are read under `ste.lazyjava.logging.FileLogHandler.`: `pattern`, `limit`, `count`, `append`, `level`, `filter`, `formatter`, `encoding`, `application`, `qualifier`, `organization`
  - [ ] Without `formatter`, `SimpleFormatter` is used; without `append`, the file is appended to
* **Pattern tokens**:
  - [ ] `${dataDir}`, `${cacheDir}`, `${configDir}`: the system folders of the application (via directories-jvm, from `application`, `qualifier`, `organization`)
  - [ ] `${application}`: the configured application name
  - [ ] `${env.NAME}`: an environment variable; any other `${name}` is a system property
  - [ ] A token that cannot be resolved, or a system-folder token without `application`, fails with a message naming the token
  - [ ] Expanded values are safe in a `FileHandler` pattern: `\` becomes `/` and `%` is escaped; the literal `%g`, `%u`, `%h`, `%t` of the pattern keep their JDK meaning
  - [ ] Default pattern `${dataDir}/logs/${application}.%g.log`
* **File handling**:
  - [ ] The folder of the log file is created when missing
  - [ ] Rotation with `limit` and `count` as in `FileHandler`; defaults 1 000 000 bytes and 3 files when not set
  - [ ] If the file cannot be created the constructor throws `IOException`; the `LogManager` reports it and the remaining handlers keep working
* **Build & documentation**:
  - [ ] `directories-jvm` added as a dependency of `lazyjava-core`; code stays Java 11 compatible
  - [ ] The configuration, the tokens and the failure behaviour are documented in the class Javadoc and in the README/docs of the module
  - [ ] JUnit 5 + AssertJ tests for each capability above

## Out-of-Scope / Future Enhancements
* A one-line log formatter (timestamp, level, message, stack trace), as used by WakeMeApp; to be scoped on its own
* Changing the log file path of an existing handler at runtime, or re-reading the configuration while running
* Fallback to another folder (temp, home) when the log folder is not usable
* Module-path (JPMS) support: the handler is loaded by the `LogManager` from the classpath; exporting the package is not part of this scope
* Other tokens (e.g. state or runtime directories) and time-based or compressed rotation
* Other handlers (console, socket, rolling by date) and the logger levels, which stay in `logging.properties`
* Migrating WakeMeApp to this handler (it depends on a published `lazyjava-core` and belongs to that project)

## Technical Context & Infrastructure
* **Development Toolchain**: Java 11 (module source/target) + Maven; JUnit 5, AssertJ; `java.util.logging` only; `dev.dirs:directories` 26 (MPL-2.0)
* **Target Systems/Platforms**: JVM library on the classpath; every platform supported by directories-jvm (Linux, macOS, Windows)
* **Project Reference Docs**:
  - Project Overview: `lazyjava-core/uzz/project/overview.md`
  - Coding Standard: `lazyjava-core/uzz/project/coding_standard.md`
  - Development Framework: `lazyjava-core/uzz/project/development_framework.md`

### Keywords
- scope
- logging
- file-log-handler
- java-util-logging
- directories
- lazyjava-core
