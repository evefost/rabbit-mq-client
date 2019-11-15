package test.com.eve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"test.com.eve", "com.eve"})
public class TestBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestBootApplication.class);
    }

}