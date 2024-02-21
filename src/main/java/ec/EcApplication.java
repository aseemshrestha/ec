package ec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"ec.*"})
@EntityScan(basePackages = "ec.*")
@EnableJpaRepositories(basePackages = {"ec.repository*"})
@EnableAsync
public class EcApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcApplication.class, args);

    }
}
