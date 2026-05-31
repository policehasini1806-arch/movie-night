package cohort.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieNightApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieNightApplication.class, args);
        System.out.println("🎬 Movie Night Decision Maker is running on http://localhost:8080");
    }
}
