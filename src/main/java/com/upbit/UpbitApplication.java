package com.upbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//(exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
@SpringBootApplication
public class UpbitApplication{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UpbitApplication.class, args);
    }

}
