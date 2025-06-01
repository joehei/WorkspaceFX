# WorkspaceFX

A an IntelliJ like workspace written in [JavaFX](https://github.com/openjdk/jfx). Designed to give a new application a
quick start. (The UI functionality is similar, it is not the goal to create a new IDE).

Java 21+

![alt text](/images/WorspaceFXDark.gif)

## Features

* Simple
* Theme support [AtlantaFX](https://github.com/mkpaz/atlantafx)
* Icon support [Ikonli](https://github.com/kordamp/ikonli)
* Comes with default plugins
  * Analog clock
    * Shows date and time
  * Digital clock
    * Shows the time
  * Calendar
    * Shows local date
  * Logging
    * Comes with a [log4j2](https://github.com/apache/logging-log4j2) / [SLF4J](https://github.com/qos-ch/slf4j) appender
    * Debug log can be activated via button
    * Log tailing
    * Filter text
    * Filter by type
    * Double click a message to open a detail view
  * JavaFX Property generator
    * A JavaFX developer plugin, generates JavaFX property getter and setter
  * Notificatons
* Drag an drop plugins
* Easy creation of custom plugins

## Getting started

* Configure the logging
  * Add a log4j config xml to your project dir.
  * Initialize the logger

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="logPath">target/cucumber-logs</Property>
        <Property name="rollingFileName">cucumber</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%style{%date{DEFAULT}}{yellow} [%t] %highlight{%-5level}{FATAL=bg_red, ERROR=red, WARN=yellow, INFO=green} %logger{36} - %message\n"/>
        </Console>
        <RollingFile name="rollingFile" fileName="${logPath}/${rollingFileName}.log" filePattern="${logPath}/${rollingFileName}_%d{yyyy-MM-dd}.log">
            <PatternLayout pattern="[%highlight{%-5level}] %d{DEFAULT} %c{1}.%M() - %msg%n%throwable{short.lineNumber}" />
            <Policies>
                <!-- Causes a rollover if the log file is older than the current JVM's start time -->
                <OnStartupTriggeringPolicy />
                <!-- Causes a rollover once the date/time pattern no longer applies to the active file -->
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
            </Policies>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Root level="INFO" additivity="false">
            <AppenderRef ref="console" />
            <!--<AppenderRef ref="rollingFile" />-->
        </Root>
    </Loggers>
</Configuration>
```
```java
public static void main(String[] args) {
  Configurator.initialize(null, "log4j2.xml");
  launch();
}
```