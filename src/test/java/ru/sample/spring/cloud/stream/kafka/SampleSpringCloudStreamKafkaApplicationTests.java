package ru.sample.spring.cloud.stream.kafka;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("embedded-kafka")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SampleSpringCloudStreamKafkaApplicationTests {

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1);

    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeClass
    public static void setUp() {
        System.setProperty("spring.embedded.kafka.zkNodes", embeddedKafka.getZookeeperConnectionString());
    }

    @Test
    public void contextLoads() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(5 * 1000);
                restTemplate.getForObject("http://localhost:8080/send?message=Hello-" + i, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
