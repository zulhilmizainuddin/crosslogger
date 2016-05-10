package com.github.zulhilmizainuddin.crosslogger;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public final class Logger {
    public static void Log(String log) {
        String javaVersion = System.getProperty("java.version");

        if (javaVersion.equals("0")) {
            LogAndroid(log);
        }
        else {
            LogJVM(log);
        }
    }

    private static void LogJVM(String log) {
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

    private static void LogAndroid(String log) {
        try {
            Class<?> logClass = Class.forName("android.util.log");
            Method infoMethod = logClass.getDeclaredMethod("i", String.class, String.class);

            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("config.properties");

            properties.load(inputStream);

            infoMethod.invoke(null, properties.getProperty("tag"), log);
        }
        catch (ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException |
                IOException ex) {}
    }
}
