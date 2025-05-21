package az.edu.msexpense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsExpenseApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsExpenseApplication.class, args);
    }
}