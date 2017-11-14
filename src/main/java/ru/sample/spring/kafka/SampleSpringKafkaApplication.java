package ru.sample.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@EnableKafka
@SpringBootApplication
public class SampleSpringKafkaApplication {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SampleSpringKafkaApplication.class, args);
    }

    @GetMapping("/send")
    public void send(@RequestParam String message) throws ExecutionException, InterruptedException {
        log.info("Forward message '{}'", message);
        kafkaTemplate.sendDefault(message);
        kafkaTemplate.flush();
    }

    @KafkaListener(topics = {"topic-1"})
    public void handle(@Payload String message) {
        log.info("Handle message '{}'", message);
    }
}
