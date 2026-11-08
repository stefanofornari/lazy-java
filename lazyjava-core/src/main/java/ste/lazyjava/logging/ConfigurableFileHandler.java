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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

/**
 * A {@link FileHandler} that can be fully configured from the JUL properties
 * file, including the folder of the log file.
 * <p>
 * The standard {@code FileHandler} only knows {@code %h}, {@code %t}, {@code %u}
 * and {@code %g} in its pattern and opens the file in its constructor, so the
 * folder cannot be changed afterwards. This handler expands {@code ${...}}
 * tokens in the pattern <em>before</em> the file is opened, creates the folder
 * if needed, and then behaves exactly as a {@code FileHandler} (rotation,
 * {@code %g}, level, formatter, encoding, filter).
 * <p>
 * Properties are read from the {@code LogManager} under this class name:
 * <pre>
 * handlers = java.util.logging.ConsoleHandler, ste.lazyjava.logging.ConfigurableFileHandler
 * ste.lazyjava.logging.ConfigurableFileHandler.application = MyApp
 * ste.lazyjava.logging.ConfigurableFileHandler.pattern     = ${dataDir}/logs/${application}.%g.log
 * ste.lazyjava.logging.ConfigurableFileHandler.limit       = 1000000
 * ste.lazyjava.logging.ConfigurableFileHandler.count       = 3
 * ste.lazyjava.logging.ConfigurableFileHandler.append      = true
 * ste.lazyjava.logging.ConfigurableFileHandler.level       = ALL
 * ste.lazyjava.logging.ConfigurableFileHandler.formatter   = java.util.logging.SimpleFormatter
 * ste.lazyjava.logging.ConfigurableFileHandler.encoding    = UTF-8
 * </pre>
 * Tokens: {@code ${dataDir}}, {@code ${cacheDir}}, {@code ${configDir}} (system
 * folders of the application, via directories-jvm), {@code ${application}},
 * {@code ${env.NAME}} (environment variable) and any other name is looked up as
 * a system property. An unknown token makes the handler fail to build.
 * <p>
 * The size is always bounded: if {@code limit} or {@code count} are not set,
 * {@link #DEFAULT_LIMIT} and {@link #DEFAULT_COUNT} apply. {@code qualifier}
 * and {@code organization} (both empty by default) refine the system folders.
 */
public class ConfigurableFileHandler extends FileHandler {

    public static final int DEFAULT_LIMIT = 1_000_000;
    public static final int DEFAULT_COUNT = 3;
    public static final String DEFAULT_APPLICATION = "app";
    public static final String DEFAULT_PATTERN = "${dataDir}/logs/${application}.%g.log";

    private static final String PREFIX = ConfigurableFileHandler.class.getName() + ".";

    public ConfigurableFileHandler() throws IOException {
        super(
            preparePattern(),
            intProperty("limit", DEFAULT_LIMIT),
            intProperty("count", DEFAULT_COUNT),
            booleanProperty("append", true)
        );
    }

    /**
     * Expands the {@code ${...}} tokens of the template. Values are made safe
     * for a {@code FileHandler} pattern (separators normalised to {@code /},
     * {@code %} escaped); the literal text of the template, e.g. {@code %g}, is
     * left untouched.
     */
    static String expand(final String template) throws IOException {
        final StringBuilder out = new StringBuilder();
        int from = 0;
        for (int start; (start = template.indexOf("${", from)) >= 0; ) {
            final int end = template.indexOf('}', start);
            if (end < 0) {
                throw new IOException("Unterminated token in log pattern: " + template);
            }
            out.append(template, from, start);
            final String name = template.substring(start + 2, end);
            out.append(escape(resolve(name, template)));
            from = end + 1;
        }
        return out.append(template.substring(from)).toString();
    }

    private static String resolve(final String name, final String template) throws IOException {
        String value;
        switch (name) {
            case "application":
                value = property("application", DEFAULT_APPLICATION);
                break;
            case "dataDir":
                value = directories().dataLocalDir;
                break;
            case "cacheDir":
                value = directories().cacheDir;
                break;
            case "configDir":
                value = directories().configDir;
                break;
            default:
                value = name.startsWith("env.")
                      ? System.getenv(name.substring(4))
                      : System.getProperty(name);
        }
        if (value == null) {
            throw new IOException("Unknown token ${" + name + "} in log pattern " + template);
        }
        return value;
    }

    private static ProjectDirectories directories() {
        return ProjectDirectories.from(
            property("qualifier", ""),
            property("organization", ""),
            property("application", DEFAULT_APPLICATION)
        );
    }

    private static String escape(final String value) {
        return value.replace('\\', '/').replace("%", "%%");
    }

    private static String preparePattern() throws IOException {
        final String pattern = expand(property("pattern", DEFAULT_PATTERN));
        createFolderOf(pattern);
        return pattern;
    }

    private static void createFolderOf(final String pattern) throws IOException {
        final int slash = pattern.lastIndexOf('/');
        if (slash <= 0) {
            return;
        }
        final String raw = pattern.substring(0, slash);
        if (raw.replace("%%", "").replace("%h", "").replace("%t", "").indexOf('%') >= 0) {
            return; // %g or %u in a folder name: leave it to FileHandler
        }
        final String folder = raw
            .replace("%%", "\0")
            .replace("%h", System.getProperty("user.home"))
            .replace("%t", System.getProperty("java.io.tmpdir"))
            .replace("\0", "%");
        Files.createDirectories(Paths.get(folder));
    }

    private static String property(final String name, final String defaultValue) {
        final String value = LogManager.getLogManager().getProperty(PREFIX + name);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    private static int intProperty(final String name, final int defaultValue) {
        try {
            return Integer.parseInt(property(name, String.valueOf(defaultValue)));
        } catch (NumberFormatException x) {
            return defaultValue;
        }
    }

    private static boolean booleanProperty(final String name, final boolean defaultValue) {
        return Boolean.parseBoolean(property(name, String.valueOf(defaultValue)));
    }
}
