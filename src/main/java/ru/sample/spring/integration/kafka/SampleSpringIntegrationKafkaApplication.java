package ru.sample.spring.integration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.kafka.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Slf4j
@SpringBootApplication
public class SampleSpringIntegrationKafkaApplication {

    @Autowired
    private ProducerFactory<String, String> producerFactory;
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    public static void main(String[] args) {
        SpringApplication.run(SampleSpringIntegrationKafkaApplication.class, args);
    }

    @Bean
    public IntegrationFlow sendFlow() {
        return IntegrationFlows
                .from(Http
                        .inboundChannelAdapter("/send")
                        .requestMapping(s -> s.methods(HttpMethod.GET))
                        .payloadExpression("#requestParams.message[0]"))
                .handle((String payload, Map<String, Object> headers) -> {
                    log.info("Forward message '{}'", payload);
                    return payload;
                })
                .handle(Kafka
                        .outboundChannelAdapter(producerFactory)
                        .topic("topic-1")
                        .sync(true))
                .get();
    }

    @Bean
    public IntegrationFlow handleFlow() {
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(consumerFactory, "topic-1"))
                .handle(message -> log.info("Handle message '{}'", message.getPayload()))
                .get();
    }
}
