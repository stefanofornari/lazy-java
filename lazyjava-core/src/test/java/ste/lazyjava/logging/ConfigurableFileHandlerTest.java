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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class ConfigurableFileHandlerTest {

    private static final String P = ConfigurableFileHandler.class.getName() + ".";

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

    private static String slash(final Path p) {
        return p.toString().replace('\\', '/');
    }

    private List<Path> files() throws IOException {
        try (Stream<Path> s = Files.walk(dir)) {
            return s.filter(Files::isRegularFile).sorted().collect(Collectors.toList());
        }
    }

    @Test
    void writes_to_the_pattern_given_in_the_properties() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log", P + "formatter = java.util.logging.SimpleFormatter");

        final ConfigurableFileHandler h = new ConfigurableFileHandler();
        h.publish(new java.util.logging.LogRecord(Level.INFO, "hello"));
        h.close();

        then(Files.readString(dir.resolve("app.0.log"))).contains("hello");
    }

    @Test
    void expands_dataDir_with_the_system_data_folder_of_the_application() throws Exception {
        final Path expected = Path.of(
            ProjectDirectories.from("", "", "lazytest").dataLocalDir
        );
        configure(P + "application = lazytest", P + "pattern = ${dataDir}/x.%g.log");

        final ConfigurableFileHandler h = new ConfigurableFileHandler();
        h.close();

        then(expected.resolve("x.0.log")).exists();
        Files.deleteIfExists(expected.resolve("x.0.log"));
        Files.deleteIfExists(expected.resolve("x.0.log.lck"));
    }

    @Test
    void expands_system_properties() throws Exception {
        System.setProperty("lazytest.dir", slash(dir));
        try {
            configure(P + "pattern = ${lazytest.dir}/p.%g.log");
            new ConfigurableFileHandler().close();
        } finally {
            System.clearProperty("lazytest.dir");
        }

        then(dir.resolve("p.0.log")).exists();
    }

    @Test
    void expands_environment_variables_with_the_env_prefix() throws Exception {
        final String path = System.getenv("PATH");
        then(path).isNotNull();

        then(ConfigurableFileHandler.expand("${env.PATH}"))
            .isEqualTo(path.replace('\\', '/').replace("%", "%%"));
    }

    @Test
    void creates_the_missing_folder() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/a/b/c/app.%g.log");

        new ConfigurableFileHandler().close();

        then(dir.resolve("a/b/c/app.0.log")).exists();
    }

    @Test
    void escapes_percent_in_expanded_values() throws Exception {
        final Path odd = dir.resolve("100%dir");
        System.setProperty("lazytest.dir", slash(odd));
        try {
            configure(P + "pattern = ${lazytest.dir}/app.%g.log");
            new ConfigurableFileHandler().close();
        } finally {
            System.clearProperty("lazytest.dir");
        }

        then(odd.resolve("app.0.log")).exists();
    }

    @Test
    void fails_clearly_on_an_unknown_token() throws Exception {
        configure(P + "pattern = ${nothing.here}/app.%g.log");

        thenThrownBy(ConfigurableFileHandler::new)
            .isInstanceOf(IOException.class)
            .hasMessageContaining("${nothing.here}");
    }

    @Test
    void takes_level_and_formatter_from_its_own_properties() throws Exception {
        configure(
            P + "pattern = " + slash(dir) + "/app.%g.log",
            P + "level = WARNING",
            P + "formatter = java.util.logging.SimpleFormatter"
        );

        final ConfigurableFileHandler h = new ConfigurableFileHandler();
        h.close();

        then(h.getLevel()).isEqualTo(Level.WARNING);
        then(h.getFormatter()).isInstanceOf(java.util.logging.SimpleFormatter.class);
    }

    @Test
    void is_bounded_by_default_even_if_limit_and_count_are_not_configured() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log", P + "limit = 2000", P + "count = 2");

        final ConfigurableFileHandler h = new ConfigurableFileHandler();
        for (int i = 0; i < 500; i++) {
            h.publish(new java.util.logging.LogRecord(Level.INFO, "line " + i + " ................................"));
        }
        h.close();

        then(files().stream().filter(f -> f.toString().endsWith(".log")).count()).isLessThanOrEqualTo(2);
    }

    @Test
    void has_a_default_cap_when_nothing_is_configured() throws Exception {
        configure(P + "pattern = " + slash(dir) + "/app.%g.log");

        final ConfigurableFileHandler h = new ConfigurableFileHandler();
        h.close();

        then(ConfigurableFileHandler.DEFAULT_LIMIT).isPositive();
        then(ConfigurableFileHandler.DEFAULT_COUNT).isGreaterThan(0);
    }

    @Test
    void can_be_loaded_by_the_log_manager_through_the_handlers_property() throws Exception {
        configure(
            "handlers = " + ConfigurableFileHandler.class.getName(),
            P + "pattern = " + slash(dir) + "/app.%g.log",
            P + "formatter = java.util.logging.SimpleFormatter"
        );

        Logger.getLogger("ste.lazyjava.logging.test").info("through the manager");
        LogManager.getLogManager().reset(); // closes and flushes the handlers

        then(Files.readString(dir.resolve("app.0.log"))).contains("through the manager");
    }
}
