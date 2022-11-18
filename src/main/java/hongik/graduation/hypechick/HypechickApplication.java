package hongik.graduation.hypechick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HypechickApplication {

	public static void main(String[] args) {
		SpringApplication.run(HypechickApplication.class, args);
	}

}
