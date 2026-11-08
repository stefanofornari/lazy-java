# Notes — logging/US-000010

1. The spike `ConfigurableFileHandler` became `FileLogHandler`; settings are read under the class name because `FileHandler` reads level, filter, formatter and encoding from `getClass().getName()`.
2. `FileHandler` sets an XML formatter when none is configured; the constructor replaces it with `SimpleFormatter` in that case only.
3. Verified here with a small shim of JUnit/AssertJ and a stand-in for `ProjectDirectories` (Maven Central is not reachable from the authoring environment): all 20 tests of `FileLogHandlerTest` passed. Still to be run with `mvn test`.
