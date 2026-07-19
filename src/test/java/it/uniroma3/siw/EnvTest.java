package it.uniroma3.siw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SiwVideogiochiApplication.class)
public class EnvTest {

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Test
    public void printEnv() {
        System.out.println("====== TEST DB_PASSWORD ======");
        System.out.println("Resolved DB_PASSWORD is: " + dbPassword);
        System.out.println("==============================");
    }
}
