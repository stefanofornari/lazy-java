# File log handler

`ste.lazyjava.logging.FileLogHandler` (module `lazyjava-core`) is a `java.util.logging` file handler that is configured **entirely from the standard `logging.properties`**, including the folder of the log file. Only the logger levels remain a matter of code.

## Why

The JDK `FileHandler` opens its file in the constructor and understands only `%h`, `%t`, `%u` and `%g` in its pattern. The folder where an application should keep its log (for example the platform's application data folder) cannot be written in the properties file, so it is usually added in code. `FileLogHandler` expands `${...}` tokens in the pattern *before* the file is opened, creates the folder when missing, and then behaves as a `FileHandler`: rotation, `%g`, level, filter, encoding.

## Setup

Add the dependency (it brings [directories-jvm](https://codeberg.org/dirs/directories-jvm), used to find the system folders):

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>lazyjava-core</artifactId>
    <version><!-- first release that includes FileLogHandler --></version>
</dependency>
```

List the handler in `logging.properties` and start the JVM so that JUL reads that file:

```properties
handlers = java.util.logging.ConsoleHandler, ste.lazyjava.logging.FileLogHandler
.level = INFO

ste.lazyjava.logging.FileLogHandler.application = MyApp
ste.lazyjava.logging.FileLogHandler.pattern     = ${dataDir}/logs/${application}.%g.log
ste.lazyjava.logging.FileLogHandler.limit       = 1000000
ste.lazyjava.logging.FileLogHandler.count       = 3
ste.lazyjava.logging.FileLogHandler.formatter   = java.util.logging.SimpleFormatter
```

```
java -Djava.util.logging.config.file=logging.properties -jar app.jar
```

The `LogManager` loads the handler from the classpath through its public no-argument constructor. (Module-path/JPMS use is not supported.)

## Settings

All settings are read under `ste.lazyjava.logging.FileLogHandler.`. The settings of the plain `java.util.logging.FileHandler` are not used.

| Setting | Default | Meaning |
|---|---|---|
| `pattern` | `${dataDir}/logs/${application}.%g.log` | Log file pattern, with tokens (below) and the usual `%g`, `%u`, `%h`, `%t`, `%%` |
| `limit` | `1000000` | Maximum bytes per file |
| `count` | `3` | Number of files kept |
| `append` | `true` | Append to an existing file |
| `formatter` | `java.util.logging.SimpleFormatter` | Formatter class (the JDK default would be XML) |
| `level`, `filter`, `encoding` | as `FileHandler` | |
| `application` | none | Application name; **required** by the system folder tokens |
| `qualifier`, `organization` | empty | Refine the system folders (see directories-jvm) |

The size is always bounded: a missing, invalid or too small `limit` or `count` falls back to the defaults, so the log never grows indefinitely.

## Tokens

| Token | Expands to |
|---|---|
| `${dataDir}` | The (machine-local) data folder of the application on the current platform |
| `${cacheDir}` | The cache folder of the application |
| `${configDir}` | The configuration folder of the application |
| `${application}` | The `application` setting |
| `${env.NAME}` | The environment variable `NAME` |
| `${name}` | Any other name: the system property `name` |

Expanded values are safe in a `FileHandler` pattern: `\` becomes `/` and `%` is escaped. The literal `%g`, `%u`, `%h` and `%t` written in the pattern keep their JDK meaning. The folder of the file is created when it does not exist.

## When something is wrong

If the file cannot be created, a token cannot be resolved (the message names the token), the token is not terminated, or `application` is missing for a system folder token, the constructor throws an `IOException`. The `LogManager` reports it on `System.err` and the other handlers, such as the console, keep working. There is no fallback location: fix the configuration.
