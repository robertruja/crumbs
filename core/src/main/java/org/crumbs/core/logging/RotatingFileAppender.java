package org.crumbs.core.logging;

import org.crumbs.core.logging.exeption.LoggerInitializationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RotatingFileAppender {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final File dir;
    private final int daysBack;
    private final String logFilePrefix;

    public RotatingFileAppender(String locationDirPath, int daysBack, String logFilePrefix) {
        try {
            File file = new File(new URI("file://" + locationDirPath));
            if(!file.exists()) {
                file.mkdirs();
            }
            this.daysBack = daysBack;
            this.dir = file;
            this.logFilePrefix = Objects.requireNonNullElseGet(logFilePrefix, dir::getName);
        } catch (URISyntaxException e) {
            throw new LoggerInitializationException("Invalid path format", e);
        }
    }

    public void appendForDate(Date date, String message) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rotateAndGet(date), true));
            writer.write(message);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File rotateAndGet(Date date) {
        String name = logFilePrefix + "." + FORMAT.format(date) + ".log";
        File existing = null;
        File oldest = null;
        long lastModified = 0;
        int count = 0;
        for(File log: dir.listFiles()) {
            if(log.getName().equals(name)) {
                existing = log;
            }
            if(lastModified == 0 || log.lastModified() < lastModified) {
                lastModified = log.lastModified();
                oldest = log;
            }
            count++;
        }
        if(count > daysBack && oldest != null) {
            oldest.delete();
        }
        if(existing == null) {
            return new File(dir.getAbsolutePath() + File.separator + name);
        }
        return existing;
    }
}
