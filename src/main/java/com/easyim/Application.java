package com.easyim;

import com.easyim.broker.SocketIOBroker;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * springboot-main
 */
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.easyim.config",
        "com.easyim.controller",
        "com.easyim.service",
        "com.easyim.broker",
        "com.easyim.filter",
        "com.exception"})
@MapperScan("com.easyim.mapper*")
public class Application {
    protected final static Logger logger = LoggerFactory.getLogger(Application.class);
    private static ApplicationContext context;
    public static ApplicationContext getContext(){
        return context;
    }
    public static void setContext(ApplicationContext context){
        context = context;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext c = app.run(args);
        context = c;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SocketIOBroker socketIOBroker = context.getBean(SocketIOBroker.class);
            socketIOBroker.destroy();
        }));
    }

}
