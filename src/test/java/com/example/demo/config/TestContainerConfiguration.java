package com.example.demo.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfiguration {
    public static GenericContainer mongo = new GenericContainer(DockerImageName.parse("mongo:5.0.6"))
            .withExposedPorts(27017);

    static {
        mongo.start();
    }

    static class TestEnvironmentsInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.port=" + mongo.getMappedPort(27017)
            );
            values.applyTo(applicationContext);
        }
    }
}
