package net.lemixeur.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class ProblemJsonConfiguration {

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule().withStackTraces(false),
                new ConstraintViolationProblemModule());
    }

}