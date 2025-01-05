package org.hometree.workspacefx.plugin.logging;

import javafx.application.Application;
import javafx.application.Platform;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Plugin(name = "WSPLog4j2Appender",
        category = Core.CATEGORY_NAME,
        elementType = org.apache.logging.log4j.core.Appender.ELEMENT_TYPE)
public class WSPLog4j2Appender extends AbstractAppender {

    private final List<WSPLog4j2> controls = new ArrayList<>();

    protected WSPLog4j2Appender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    protected WSPLog4j2Appender(String name, Filter filter) {
        super(name, filter, null);
    }

    @PluginFactory
    public static WSPLog4j2Appender createAppender(@PluginAttribute("name") String name, @PluginElement("Filter") Filter filter) {
        return new WSPLog4j2Appender(name, filter);
    }

    public List<WSPLog4j2> getControls() {
        return controls;
    }

    @Override
    public void append(LogEvent event) {
        ZonedDateTime ts = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeMillis()), ZoneId.systemDefault());
        WSPLog4j2LogMessage message = new WSPLog4j2LogMessage(ts, event.getLevel(), event.getMessage().getFormattedMessage(), event.getThrown());
        for (WSPLog4j2 control : controls) {
            if (Platform.isFxApplicationThread()) {
                control.getMessages().add(message);
            } else {
                Platform.runLater(() -> control.getMessages().add(message));
            }
        }
    }
}
