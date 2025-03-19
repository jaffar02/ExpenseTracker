package com.example.userservice.eventConsumer;

import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceConsumer {
    private UserRepository userRepository;

    @Autowired
    public AuthServiceConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Object eventData) {
        try {
            log.info("Received message: {}", eventData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
