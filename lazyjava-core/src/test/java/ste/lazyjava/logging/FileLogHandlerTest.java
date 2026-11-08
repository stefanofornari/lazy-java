/*
 * Copyright 2026 the original author or authors from the lazy-java project
 * (https://github.com/stefanofornari/lazy-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ste.lazyjava.logging;

import dev.dirs.ProjectDirectories;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class FileLogHandlerTest {

    private static final String P = FileLogHandler.class.getName() + ".";

    @TempDir
    Path dir;

    @AfterEach
    void restore() throws Exception {
        LogManager.getLogManager().reset();
        LogManager.getLogManager().readConfiguration();
    }

    private void configure(final String... lines) throws IOException {
        final String text = String.join("\n", lines).replace("\\", "\\\\");
        LogManager.getLogManager().readConfiguration(
            new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1))
        );
    }

    private String slash(final Path p) {
        return p.toString().replace('\\', '/');
    }

    private long logFiles(final Path folder) throws IOException {
        try (Stream<Path> s = Files.list(folder)) {
            return s.filter(f -> f.toString().endsWith(".log")).count();
        }
    }

    // --- US-000010: configuration from logging.properties ---

    @Test
    void is_loaded_by_the_log_manager_through_the_handlers_property() throws Exception {
        configure(
            "handlers = " + FileLogHandler.class.getName(),
            P + "pattern = " + slash(dir) + "/app.%g.log"
        );

        Logger.getLogger("ste.lazyjava.logging.test").info("through the manager");
        LogManager.getLogManager().reset(); // closes and flushes the handlers

        final String content = Files.readString(dir.resolve("app.0.log"));
        then(content).contains("through the manager");
        then(content).doesNotContain("<?xml"); // SimpleFormatter, not the XML default
    }

    @Test
    void takes_its_settings_from_its_own_namespace() throws Exception {
        configure(
            P + "pattern = " + slash(dir) + "/app.%g.log",
            P + "level = WARNING",
            P + "formatter = java.util.logging.SimpleFormatter",
            P + "encoding = UTF-8"
        );

        final FileLogHandler h = new FileLogHandler();
        h.close();

        then(h.getLevel()).isEqualTo(Level.WARNING);
        then(h.getFormatter()).isInstanceOf(SimpleFormatter.class);
        then(h.getEncoding()).isEqualTo("UTF-8");
    }

    @Test
    void ignores_the_settings_of_the_plain_file_handler() throws Exception {
        configure(
            P + "pattern = " + slash(dir) + "/app.%g.log",
            "java.util.logging.FileHandler.level = SEVERE",
            "java.util.logging.FileHandler.pattern = " + slash(dir) + "/other.log"
        );

        final FileLogHandler h = new FileLogHandler();
        h.close();

        then(h.getLevel()).isNotEqualTo(Level.SEVERE);
        then(dir.resolve("app.0.log")).exists();
        then(dir.resolve("other.log")).doesNotExist();
    }

    // --- US-000011: bounded size ---

    @Test
    void rotates_with_the_configured_limit_and_count() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log", P + "limit = 2000", P + "count = 2");

        final FileLogHandler h = new FileLogHandler();
        for (int i = 0; i < 500; i++) {
            h.publish(new LogRecord(Level.INFO, "line " + i + " ................................"));
        }
        h.close();

        then(logFiles(dir)).isLessThanOrEqualTo(2);
    }

    @Test
    void is_bounded_by_default_when_limit_and_count_are_not_configured() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log");

        final FileLogHandler h = new FileLogHandler();
        final String filler = "x".repeat(200);
        for (int i = 0; i < 25_000; i++) { // well above 3 x 1 MB
            h.publish(new LogRecord(Level.INFO, filler));
        }
        h.close();

        then(FileLogHandler.DEFAULT_LIMIT).isEqualTo(1_000_000);
        then(FileLogHandler.DEFAULT_COUNT).isEqualTo(3);
        then(logFiles(dir)).isLessThanOrEqualTo(3);
        try (Stream<Path> s = Files.list(dir)) {
            then(s.filter(f -> f.toString().endsWith(".log")).mapToLong(f -> f.toFile().length()).max().getAsLong())
                .isLessThanOrEqualTo(1_100_000L);
        }
    }

    @Test
    void falls_back_to_the_defaults_when_limit_and_count_are_not_numbers() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log", P + "limit = lots", P + "count = few");

        final FileLogHandler h = new FileLogHandler();
        final String filler = "x".repeat(200);
        for (int i = 0; i < 25_000; i++) {
            h.publish(new LogRecord(Level.INFO, filler));
        }
        h.close();

        then(logFiles(dir)).isLessThanOrEqualTo(3);
    }

    // --- US-000012: folder creation ---

    @Test
    void creates_the_missing_folders() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/a/b/c/app.%g.log");

        new FileLogHandler().close();

        then(dir.resolve("a/b/c/app.0.log")).exists();
    }

    @Test
    void creates_a_folder_whose_name_contains_a_literal_percent() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/100%%dir/app.%g.log");

        new FileLogHandler().close();

        then(dir.resolve("100%dir/app.0.log")).exists();
    }

    // --- US-000013: system folders ---

    @Test
    void expands_dataDir_with_the_system_data_folder_of_the_application() throws Exception {
        final Path expected = Path.of(ProjectDirectories.from("", "", "lazytest").dataLocalDir);
        configure(P + "application = lazytest", P + "pattern = ${dataDir}/x.%g.log");

        new FileLogHandler().close();

        then(expected.resolve("x.0.log")).exists();
        Files.deleteIfExists(expected.resolve("x.0.log"));
        Files.deleteIfExists(expected.resolve("x.0.log.lck"));
    }

    @Test
    void expands_cacheDir_and_configDir_and_application() throws Exception {
        final ProjectDirectories pd = ProjectDirectories.from("", "", "lazytest");

        configure(P + "application = lazytest");

        then(FileLogHandler.expand("${cacheDir}")).isEqualTo(pd.cacheDir.replace('\\', '/').replace("%", "%%"));
        then(FileLogHandler.expand("${configDir}")).isEqualTo(pd.configDir.replace('\\', '/').replace("%", "%%"));
        then(FileLogHandler.expand("${application}")).isEqualTo("lazytest");
    }

    @Test
    void uses_qualifier_and_organization_for_the_system_folders() throws Exception {
        final ProjectDirectories pd = ProjectDirectories.from("com", "acme", "lazytest");

        configure(P + "application = lazytest", P + "qualifier = com", P + "organization = acme");

        then(FileLogHandler.expand("${dataDir}")).isEqualTo(pd.dataLocalDir.replace('\\', '/').replace("%", "%%"));
    }

    @Test
    void uses_dataDir_logs_application_as_the_default_pattern() throws Exception {
        final Path expected = Path.of(ProjectDirectories.from("", "", "lazytest2").dataLocalDir, "logs");
        configure(P + "application = lazytest2");

        new FileLogHandler().close();

        then(expected.resolve("lazytest2.0.log")).exists();
        Files.deleteIfExists(expected.resolve("lazytest2.0.log"));
        Files.deleteIfExists(expected.resolve("lazytest2.0.log.lck"));
        Files.deleteIfExists(expected);
    }

    @Test
    void requires_the_application_name_for_the_system_folders() throws Exception {
        configure(P + "pattern = ${dataDir}/x.%g.log");

        thenThrownBy(FileLogHandler::new)
            .isInstanceOf(IOException.class)
            .hasMessageContaining("application");
    }

    // --- US-000014: system properties and environment ---

    @Test
    void expands_system_properties() throws Exception {
        System.setProperty("lazytest.dir", slash(dir));
        try {
            configure(P + "pattern = ${lazytest.dir}/p.%g.log");
            new FileLogHandler().close();
        } finally {
            System.clearProperty("lazytest.dir");
        }

        then(dir.resolve("p.0.log")).exists();
    }

    @Test
    void expands_environment_variables_with_the_env_prefix() throws Exception {
        final String path = System.getenv("PATH");
        then(path).isNotNull();

        then(FileLogHandler.expand("${env.PATH}"))
            .isEqualTo(path.replace('\\', '/').replace("%", "%%"));
    }

    @Test
    void takes_a_percent_in_an_expanded_value_literally() throws Exception {
        final Path odd = dir.resolve("100%dir");
        System.setProperty("lazytest.dir", slash(odd));
        try {
            configure(P + "pattern = ${lazytest.dir}/app.%g.log");
            new FileLogHandler().close();
        } finally {
            System.clearProperty("lazytest.dir");
        }

        then(odd.resolve("app.0.log")).exists();
    }

    @Test
    void fails_clearly_on_an_unknown_token() throws Exception {
        configure(P + "pattern = ${nothing.here}/app.%g.log");

        thenThrownBy(FileLogHandler::new)
            .isInstanceOf(IOException.class)
            .hasMessageContaining("${nothing.here}");
    }

    @Test
    void fails_clearly_on_an_unterminated_token() throws Exception {
        configure(P + "pattern = ${nothing/app.%g.log");

        thenThrownBy(FileLogHandler::new).isInstanceOf(IOException.class);
    }

    // --- US-000015: failures are reported by JUL, other handlers keep working ---

    @Test
    void throws_an_io_exception_when_the_file_cannot_be_created() throws Exception {
        final Path file = Files.createFile(dir.resolve("not-a-folder"));
        configure(P + "pattern = " + slash(file) + "/sub/app.%g.log");

        thenThrownBy(FileLogHandler::new).isInstanceOf(IOException.class);
    }

    @Test
    void the_log_manager_reports_the_failure_and_keeps_the_other_handlers() throws Exception {
        final Path file = Files.createFile(dir.resolve("not-a-folder"));
        configure(
            "handlers = java.util.logging.ConsoleHandler, " + FileLogHandler.class.getName(),
            P + "pattern = " + slash(file) + "/sub/app.%g.log"
        );
        final PrintStream err = System.err;
        final ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setErr(new PrintStream(captured, true));
        final Handler[] handlers;
        try {
            handlers = Logger.getLogger("").getHandlers(); // loads the handlers
        } finally {
            System.setErr(err);
        }

        then(handlers).hasSize(1);
        then(handlers[0]).isInstanceOf(java.util.logging.ConsoleHandler.class);
        then(captured.toString()).contains(FileLogHandler.class.getName());
    }
}
