package net.oscer;

import net.oscer.framework.ConfigTool;
import net.oscer.server.WebsocketStarter;
import net.oscer.service.QiNiuApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 启动类
 *
 * @author kz
 * @date 2019年3月14日16:31:05
 **/
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@PropertySource({"classpath:config/application.yml", "classpath:config/config.properties"})

public class Application extends SpringBootServletInitializer {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new QiNiuApi(ConfigTool.getProp("qiniu.access"), ConfigTool.getProp("qiniu.secret"), ConfigTool.getProp("qiniu.bucket"));
                logger.info("启动成功");
                try {
                    WebsocketStarter.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}
