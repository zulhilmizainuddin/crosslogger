package com.github.zulhilmizainuddin.crosslogger;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Logger {

    private static final String TAG = "crosslogger";

    public static void log(String log) {
        String javaVersion = System.getProperty("java.version");

        if (javaVersion.equals("0")) {
            logAndroid(log);
        }
        else {
            logJVM(log);
        }
    }

    private static void logJVM(String log) {
        try {
            Class<?> systemClass = Class.forName("java.lang.System");
            Field outField = systemClass.getDeclaredField("out");

            Class<?> printStreamClass = outField.getType();
            Method printlnMethod = printStreamClass.getDeclaredMethod("println", String.class);

            Object object = outField.get(null);
            printlnMethod.invoke(object, log);
        }
        catch (ClassNotFoundException |
                NoSuchFieldException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException ex) {}
    }

    private static void logAndroid(String log) {
        try {
            Class<?> logClass = Class.forName("android.util.Log");
            Method infoMethod = logClass.getDeclaredMethod("i", String.class, String.class);

            infoMethod.invoke(null, TAG, log);
        }
        catch (ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException ex) {}
    }
}
