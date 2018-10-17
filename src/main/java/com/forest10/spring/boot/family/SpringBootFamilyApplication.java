package com.forest10.spring.boot.family;

import com.forest10.web.annotation.EnableLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Forest10
 */
@RestController
@EnableLogFilter
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootFamilyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFamilyApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(this.getClass());
    }

    @RequestMapping("/")
    public String index(String name) {
        return "hi" + name;
    }
}
