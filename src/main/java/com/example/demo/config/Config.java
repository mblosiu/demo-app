package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            User mateusz = new User("mateusz@gmail.com", "Mateusz", "Błoszyk");
            User adam = new User("adam@gmail.com", "Adam", "Kowalski");
            userRepository.saveAll(List.of(mateusz,adam));
        };
    }
}
