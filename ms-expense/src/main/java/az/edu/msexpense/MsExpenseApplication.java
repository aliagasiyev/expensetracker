package az.edu.msexpense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class MsExpenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsExpenseApplication.class, args);
    }

}
