package ste.lazyjava.maven;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import ste.lazyjava.bytecode.NonNullConstructorAdvice;
import ste.lazyjava.bytecode.NonNullMethodAdvice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(
    name = "weave",
    defaultPhase = LifecyclePhase.PROCESS_CLASSES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class LazyJavaMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            getLog().info("No classes found to instrument.");
            return;
        }

        getLog().info("Weaving parameter validation into classes in: " + outputDirectory.getAbsolutePath());

        try {
            List<Path> classFiles;
            try (Stream<Path> stream = Files.walk(outputDirectory.toPath())) {
                classFiles = stream
                    .filter(p -> p.toString().endsWith(".class"))
                    .collect(Collectors.toList());
            }

            ClassFileLocator classFileLocator = new ClassFileLocator.Compound(
                ClassFileLocator.ForFolder.of(outputDirectory, net.bytebuddy.ClassFileVersion.ofJavaVersion(11)),
                ClassFileLocator.ForClassLoader.of(Thread.currentThread().getContextClassLoader())
            );
            TypePool typePool = TypePool.Default.of(classFileLocator);

            int instrumentedCount = 0;

            for (Path classFile : classFiles) {
                String className = getClassName(outputDirectory.toPath(), classFile);

                if (className.contains("$")) {
                    continue;
                }

                try {
                    new ByteBuddy()
                        .redefine(typePool.describe(className).resolve(), classFileLocator)
                        .visit(Advice.to(NonNullMethodAdvice.class)
                            .on(ElementMatchers.isMethod().and(ElementMatchers.not(ElementMatchers.isAbstract()))))
                        .visit(Advice.to(NonNullConstructorAdvice.class)
                            .on(ElementMatchers.isConstructor()))
                        .make()
                        .saveIn(outputDirectory);

                    instrumentedCount++;
                } catch (Exception e) {
                    getLog().warn("Failed to instrument " + className + ": " + e.getMessage());
                }
            }

            getLog().info("Successfully instrumented " + instrumentedCount + " classes.");

        } catch (IOException e) {
            throw new MojoExecutionException("Error weaving parameter validation bytecode", e);
        }
    }

    private String getClassName(Path baseDir, Path classFile) {
        Path relativePath = baseDir.relativize(classFile);
        String pathString = relativePath.toString();
        return pathString
            .substring(0, pathString.length() - ".class".length())
            .replace(File.separatorChar, '.');
    }
}
