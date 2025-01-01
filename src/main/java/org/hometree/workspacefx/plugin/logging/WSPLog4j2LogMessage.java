package org.hometree.workspacefx.plugin.logging;


import org.apache.logging.log4j.Level;

import java.time.ZonedDateTime;

public class WSPLog4j2LogMessage {

    private ZonedDateTime ts;
    private Level level;
    private String message;
    private Throwable throwable;

    public WSPLog4j2LogMessage(ZonedDateTime ts, Level level, String message, Throwable throwable) {
        this.ts = ts;
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "WSPLog4j2LogMessage{" +
                "ts=" + ts +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", throwable=" + throwable +
                '}';
    }

    public ZonedDateTime getTs() {
        return ts;
    }

    public void setTs(ZonedDateTime ts) {
        this.ts = ts;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
