package vn.com.ecomstore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

@SpringBootApplication
public class EcomstoreApplication {


	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		Path dotenvPath = Paths.get("local.env");
		if (Files.exists(dotenvPath)) {
			Dotenv dotenv = Dotenv.configure()
					.filename("local.env")
					.load();

			dotenv.entries().forEach(entry ->
					System.setProperty(entry.getKey(), entry.getValue())
			);
		}


		SpringApplication.run(EcomstoreApplication.class, args);
	}
}
