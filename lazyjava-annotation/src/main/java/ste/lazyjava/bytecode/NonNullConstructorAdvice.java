package ste.lazyjava.bytecode;

import net.bytebuddy.asm.Advice;
import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class NonNullConstructorAdvice {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Constructor<?> constructor, @Advice.AllArguments Object[] args) {
        Parameter[] parameters = constructor.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Object arg = args[i];
            Parameter param = parameters[i];
            String name = param.getName();

            if (param.isAnnotationPresent(NonNull.class) && arg == null) {
                throw new IllegalArgumentException(name + " must not be null");
            }

            if (param.isAnnotationPresent(NonBlank.class)) {
                if (arg == null || (arg instanceof String s && s.trim().isEmpty())) {
                    throw new IllegalArgumentException(name + " must be non blank");
                }
            }

            if (param.isAnnotationPresent(NonEmpty.class)) {
                if (arg == null || (arg instanceof String s && s.isEmpty())) {
                    throw new IllegalArgumentException(name + " must be non empty");
                }
            }
        }
    }
}