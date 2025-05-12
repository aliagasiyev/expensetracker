package az.edu.msanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAnalyticsApplication.class, args);
    }

}
