package ru.sample.spring.cloud.stream.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@EnableBinding({Sink.class, Source.class})
@SpringBootApplication
public class SampleSpringCloudStreamKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SampleSpringCloudStreamKafkaApplication.class);

    @Autowired
    private Source forward;

    public static void main(String[] args) {
        SpringApplication.run(SampleSpringCloudStreamKafkaApplication.class, args);
    }

    @GetMapping("/send")
    public void send(@RequestParam String message) throws UnsupportedEncodingException {
        LOG.info("Forward message: {}", message);
        forward.output().send(MessageBuilder.withPayload(message).build());
    }

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        LOG.info("Handle message: {}", message);
    }
}
