package ste.lazyjava.bytecode;

import net.bytebuddy.asm.Advice;
import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NonNullMethodAdvice {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
        System.out.println("onMethodEnter " + method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Object arg = args[i];
            Parameter param = parameters[i];
            String name = param.getName();

            System.out.println("param name " + name);

            if (param.isAnnotationPresent(NonNull.class) && arg == null) {
                throw new IllegalArgumentException(name + " must not be null");
            }

            if (param.isAnnotationPresent(NonBlank.class)) {
                System.out.println("nonBlank");
                if (arg == null || (arg instanceof String s && s.trim().isBlank())) {
                    throw new IllegalArgumentException(name + " must be non blank");
                }
            }

            if (param.isAnnotationPresent(NonEmpty.class)) {
                System.out.println("nonEmpty");
                if (arg == null || (arg instanceof String s && s.isEmpty())) {
                    throw new IllegalArgumentException(name + " must be non empty");
                }
            }
        }
    }
}