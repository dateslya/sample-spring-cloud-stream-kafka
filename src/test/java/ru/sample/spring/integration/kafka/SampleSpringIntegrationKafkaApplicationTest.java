package ru.sample.spring.integration.kafka;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@ActiveProfiles({
        "kafka",
        "embedded-kafka"
})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleSpringIntegrationKafkaApplicationTest {

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, false, "topic-1");

    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @Value("${server.port}")
    private int port;

    @Test
    public void send() throws Exception {
        for (int i = 0; i < 1; i++) {
            try {
                TimeUnit.SECONDS.sleep(3);
                restTemplate.getForObject("http://localhost:" + port + "/send?message=Hello-" + i, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}