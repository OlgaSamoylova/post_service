package ru.skillbox.diplom.alpha.microservice.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import ru.skillbox.diplom.alpha.library.core.annotation.EnableImageStorage;
import ru.skillbox.diplom.alpha.library.core.annotation.EnableOpenFeign;
import ru.skillbox.diplom.alpha.library.core.annotation.SecurityCoreConfig;

/**
* Application
*
*@author Ruslan Akbashev
*/

@EnableImageStorage
@EnableOpenFeign
@SecurityCoreConfig
@SpringBootApplication
@EnableDiscoveryClient
@EnableSpringDataWebSupport
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
