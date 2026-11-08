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
import java.util.logging.SimpleFormatter;

/**
 * A {@link FileHandler} that is configured entirely from the JUL properties file, including the
 * folder of the log file.
 * <p>
 * The standard {@code FileHandler} opens its file in the constructor and only knows {@code %h},
 * {@code %t}, {@code %u} and {@code %g} in its pattern, so the folder of an application cannot be
 * expressed in {@code logging.properties}. This handler expands {@code ${...}} tokens in the
 * pattern <em>before</em> the file is opened, creates the folder if needed, and then behaves as a
 * {@code FileHandler} (rotation, {@code %g}, level, filter, encoding). Only the logger levels
 * remain a matter of code.
 *
 * <h2>Configuration</h2>
 * The handler is created by the {@code LogManager} (public no-argument constructor, classpath) and
 * reads its settings under its own class name:
 * <pre>
 * handlers = java.util.logging.ConsoleHandler, ste.lazyjava.logging.FileLogHandler
 *
 * ste.lazyjava.logging.FileLogHandler.application = MyApp
 * ste.lazyjava.logging.FileLogHandler.pattern     = ${dataDir}/logs/${application}.%g.log
 * ste.lazyjava.logging.FileLogHandler.limit       = 1000000
 * ste.lazyjava.logging.FileLogHandler.count       = 3
 * ste.lazyjava.logging.FileLogHandler.append      = true
 * ste.lazyjava.logging.FileLogHandler.level       = ALL
 * ste.lazyjava.logging.FileLogHandler.formatter   = java.util.logging.SimpleFormatter
 * ste.lazyjava.logging.FileLogHandler.encoding    = UTF-8
 * </pre>
 * <table>
 * <caption>Settings and defaults</caption>
 * <tr><th>setting</th><th>default</th></tr>
 * <tr><td>{@code pattern}</td><td>{@value #DEFAULT_PATTERN}</td></tr>
 * <tr><td>{@code limit}</td><td>{@value #DEFAULT_LIMIT} bytes per file</td></tr>
 * <tr><td>{@code count}</td><td>{@value #DEFAULT_COUNT} files</td></tr>
 * <tr><td>{@code append}</td><td>{@code true}</td></tr>
 * <tr><td>{@code formatter}</td><td>{@link SimpleFormatter} (not the XML default of the JDK)</td></tr>
 * <tr><td>{@code level}, {@code filter}, {@code encoding}</td><td>as {@code FileHandler}</td></tr>
 * <tr><td>{@code application}, {@code qualifier}, {@code organization}</td>
 *     <td>none; {@code application} is required by the system folder tokens</td></tr>
 * </table>
 * The size is always bounded: invalid or missing {@code limit} and {@code count} fall back to the
 * defaults. The settings of the plain {@code java.util.logging.FileHandler} are not used.
 *
 * <h2>Tokens</h2>
 * <ul>
 * <li>{@code ${dataDir}}, {@code ${cacheDir}}, {@code ${configDir}}: the system folders of the
 *     application (via directories-jvm), built from {@code application}, {@code qualifier} and
 *     {@code organization}. The {@code application} setting is required for them;</li>
 * <li>{@code ${application}}: the application name;</li>
 * <li>{@code ${env.NAME}}: the environment variable {@code NAME};</li>
 * <li>{@code ${name}}: any other name is a system property.</li>
 * </ul>
 * Expanded values are safe in a pattern ({@code \} becomes {@code /}, {@code %} is escaped); the
 * literal {@code %g}, {@code %u}, {@code %h} and {@code %t} of the pattern keep their JDK meaning.
 *
 * <h2>Failures</h2>
 * If the file cannot be created, a token cannot be resolved or the application is missing, the
 * constructor throws an {@link IOException}. The {@code LogManager} reports it on {@code System.err}
 * and the other handlers (e.g. the console) keep working; there is no fallback location.
 */
public class FileLogHandler extends FileHandler {

    public static final int DEFAULT_LIMIT = 1_000_000;
    public static final int DEFAULT_COUNT = 3;
    public static final String DEFAULT_PATTERN = "${dataDir}/logs/${application}.%g.log";

    private static final String PREFIX = FileLogHandler.class.getName() + ".";

    public FileLogHandler() throws IOException {
        super(
            preparePattern(),
            positiveIntProperty("limit", 0, DEFAULT_LIMIT),
            positiveIntProperty("count", 1, DEFAULT_COUNT),
            booleanProperty("append", true)
        );
        // level, filter, formatter and encoding are read from the properties by FileHandler
        if (property("formatter", null) == null) {
            setFormatter(new SimpleFormatter());
        }
    }

    /**
     * Expands the {@code ${...}} tokens of the template. Values are made safe for a
     * {@code FileHandler} pattern; the literal text of the template is left untouched.
     */
    static String expand(final String template) throws IOException {
        final StringBuilder out = new StringBuilder();
        int from = 0;
        for (int start; (start = template.indexOf("${", from)) >= 0; ) {
            final int end = template.indexOf('}', start);
            if (end < 0) {
                throw new IOException("Unterminated token in log pattern " + template);
            }
            out.append(template, from, start);
            out.append(escape(resolve(template.substring(start + 2, end), template)));
            from = end + 1;
        }
        return out.append(template.substring(from)).toString();
    }

    private static String resolve(final String name, final String template) throws IOException {
        final String value;
        switch (name) {
            case "application":
                value = application(name);
                break;
            case "dataDir":
                value = directories(name).dataLocalDir;
                break;
            case "cacheDir":
                value = directories(name).cacheDir;
                break;
            case "configDir":
                value = directories(name).configDir;
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

    private static String application(final String token) throws IOException {
        final String application = property("application", null);
        if (application == null) {
            throw new IOException(
                "The setting " + PREFIX + "application is required to use ${" + token + "} in the log pattern"
            );
        }
        return application;
    }

    private static ProjectDirectories directories(final String token) throws IOException {
        return ProjectDirectories.from(
            property("qualifier", ""), property("organization", ""), application(token)
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

    /** An integer of at least {@code min}; anything else (missing, not a number, too small) gives the default. */
    private static int positiveIntProperty(final String name, final int min, final int defaultValue) {
        try {
            final int value = Integer.parseInt(property(name, String.valueOf(defaultValue)));
            return (value < min) ? defaultValue : value;
        } catch (NumberFormatException x) {
            return defaultValue;
        }
    }

    private static boolean booleanProperty(final String name, final boolean defaultValue) {
        return Boolean.parseBoolean(property(name, String.valueOf(defaultValue)));
    }
}
