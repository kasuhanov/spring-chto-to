package su.asgor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import su.asgor.service.LoaderService;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class Application extends SpringBootServletInitializer  implements CommandLineRunner {
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LoaderService loader;
	@Override
	public void run(String... args) throws Exception {
		log.info("Starting ftp download");
		loader.start();
	}
}
