package com.glenworsley.restdemo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository) {
        return args -> {
            log.info("Preloading " + customerRepository.save(new Customer("Bob","Down")));
            log.info("Preloading " + customerRepository.save(new Customer("Stan", "Dup")));
        };
    }
}
