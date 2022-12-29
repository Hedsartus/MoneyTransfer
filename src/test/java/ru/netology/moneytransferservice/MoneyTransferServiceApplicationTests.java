package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.netology.moneytransferservice.model.Amount;
import ru.netology.moneytransferservice.model.Confirm;
import ru.netology.moneytransferservice.model.Transfer;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyTransferServiceApplicationTests {
    final static int PORT = 8080;
    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private final GenericContainer<?> backend = new GenericContainer<>("myapp:latest")
            .withExposedPorts(PORT);

    @BeforeEach
    public void setUp() {
        backend.start();
    }

    @Test
    void moneyTransferPrepareTest() {
        Transfer transfer = new Transfer(
            "1234567891234567",
            "12/25",
            "118",
            "1111222233334444",
            new Amount(20000, "RUR"));


        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" +
                backend.getMappedPort(PORT) + "/transfer", transfer, String.class);

        String expected = "{\"operationId\":" + "\"1\"}";
        Assertions.assertEquals(expected, response.getBody());

    }

    @Test
    void confirmOperationTest() {
        Confirm confirm = new Confirm("122", "0000");

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" +
                backend.getMappedPort(PORT) + "/confirmOperation", confirm, String.class);

        String expected = "{\"message\":\"Невозможная операция, перевод №122 не существует!\"}";
        Assertions.assertEquals(expected, response.getBody());
    }
}
