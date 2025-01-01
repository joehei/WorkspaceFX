package org.hometree.workspacefx.plugin.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;

public class WSPLog4j2Utils {

    private WSPLog4j2Utils() {
    }

    public static void addAppender(WSPLog4j2 wspLog4j2) {
        addAppender(wspLog4j2, "wspLog4j2", null);
    }

    public static void addAppender(WSPLog4j2 wspLog4j2, String name, Filter filter) {
        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        WSPLog4j2Appender appender = new WSPLog4j2Appender(name, filter);
        appender.getControls().add(wspLog4j2);
        appender.start();
        lc.getConfiguration().addAppender(appender);
        lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(appender.getName()));
        lc.updateLoggers();
    }

}
