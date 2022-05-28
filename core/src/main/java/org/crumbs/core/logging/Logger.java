package org.crumbs.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;

public class Logger {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String PATTERN = "%T [%L] %C - %M";
    private static final int DAYS_BACK_DEFAULT = 30;

    private static Level level = Level.valueOf(System.getProperty("log.level", "INFO"));
    private static String packages = System.getProperty("log.packages", "");
    private static String dirPath = System.getProperty("log.dirPath", "");
    private static String filePrefix = System.getProperty("log.filePrefix");
    private static String daysBack = System.getProperty("log.daysBack");

    private Class<?> clazz;
    private Date date = new Date();
    private Writer writer;

    private boolean active = true;

    private <T> Logger(Class<T> clazz) {
        this.clazz = clazz;
        String packageName = clazz.getPackage().getName();
        if (!packages.isEmpty()) {
            active = packageName.startsWith("org.crumbs") || Arrays.stream(packages.split(","))
                    .anyMatch(packageName::startsWith);
        }
        if(!dirPath.isEmpty()) {
            RotatingFileAppender appender = new RotatingFileAppender(dirPath, daysBack != null ?
                    Integer.parseInt(daysBack) : DAYS_BACK_DEFAULT, filePrefix);
            writer = message -> appender.appendForDate(date, message);
        } else {
            writer = System.out::println;
        }
    }

    public static <U> Logger getLogger(Class<U> clazz) {
        return new Logger(clazz);
    }

    public void debug(Object message) {
        log(Level.DEBUG, message.toString());
    }

    public void debug(String message, Object... args) {
        log(Level.DEBUG, message, args);
    }

    public void info(Object message) {
        log(Level.INFO, message.toString());
    }

    public void info(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    public void warn(Object message) {
        log(Level.WARN, message.toString());
    }

    public void warn(String message, Object... args) {
        log(Level.WARN, message, args);
    }

    public void error(Object message) {
        log(Level.ERROR, message.toString());
    }

    public void error(String message, Object... args) {
        log(Level.ERROR, message, args);
    }

    public void error(String message, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        message = message + ". Underlying cause: " + sw.toString();
        log(Level.ERROR, message);
    }

    private void log(Level level, String message, Object... args) {
        if (!active || level.getOrdinal() < Logger.level.getOrdinal()) {
            return;
        }
        date.setTime(System.currentTimeMillis());
        String formatted = PATTERN.replace("%T", DATE_FORMAT.format(date));
        formatted = formatted.replace("%L", level.toString());
        formatted = formatted.replace("%C", clazz.getSimpleName());
        message = formatMessage(message, args);
        formatted = formatted.replace("%M", message);
        writer.out(formatted);
    }

    private String formatMessage(String message, Object... args) {
        String formatted = message;
        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{}", Matcher.quoteReplacement(arg.toString()));
        }
        return formatted;
    }

    private interface Writer {
        void out(String message);
    }
}
