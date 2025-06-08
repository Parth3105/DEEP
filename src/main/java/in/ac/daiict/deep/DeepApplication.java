package in.ac.daiict.deep;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeepApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeepApplication.class, args);
	}
}
