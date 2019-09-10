package org.alvin.code.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@ComponentScans({
		@ComponentScan("org.alvin.*")
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
