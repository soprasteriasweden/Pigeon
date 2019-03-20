package se.soprasteria.automatedtesting.webdriver.api.base;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.testng.Reporter;

import java.io.Serializable;
import java.sql.Timestamp;

@Plugin(name = "TestNGCustom", category = "Core", elementType = "appender", printObject = true)
public class BaseTestNGLogAppender extends AbstractAppender {

    private static volatile BaseTestNGLogAppender instance;

    public BaseTestNGLogAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    @PluginFactory
    public static BaseTestNGLogAppender createAppender(@PluginAttribute("name") String name,
                                                       @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                                       @PluginElement("Layout") Layout layout,
                                                       @PluginElement("Filters") Filter filter) {
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        instance = new BaseTestNGLogAppender(name, filter, layout);
        return instance;
    }

    public static BaseTestNGLogAppender getInstance() {
        return instance;
    }

    @Override
    public void append(final LogEvent event) {
        String timestamp  = new Timestamp(event.getTimeMillis()).toLocalDateTime().toLocalTime().toString();
        String logLevel = event.getLevel().name();
        String message = event.getMessage().getFormattedMessage();

        Reporter.log(String.format("%1$s %2$-7s %3$s",
                timestamp, logLevel, message));

    }
}