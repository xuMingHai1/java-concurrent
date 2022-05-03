package xyz.xuminghai.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;

/**
 * 2022/3/1 20:49 星期二<br/>
 * logback配置
 * @author xuMingHai
 */
public class LogbackConfig extends ContextAwareBase implements Configurator {
    @Override
    public void configure(LoggerContext loggerContext) {
        // 开始自定义实现类配置
        addInfo("开始自定义配置实现类");

        // 控制台附加器
        final ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        // 设置上下文
        consoleAppender.setContext(loggerContext);
        // 设置顶级附加器名字
        consoleAppender.setName("root");

        // 字符集
        final LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);

        // 设置带有颜色的图案布局
        final PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(loggerContext);
        patternLayout.setPattern("%d %highlight(%-5level) [%thread] --- %cyan(%logger{25}) : %msg %n");
        // 开始配置
        patternLayout.start();

        encoder.setLayout(patternLayout);

        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
    }
}
