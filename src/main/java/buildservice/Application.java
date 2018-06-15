package buildservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;



import hello.storage.StorageProperties;
import hello.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

     @Override
     public void run(String... strings) throws Exception {

         log.info("Creating tables");

         jdbcTemplate.execute("DROP TABLE builds IF EXISTS");
         jdbcTemplate.execute("CREATE TABLE builds(" +
                 "id SERIAL, toolbox VARCHAR(255), ctf VARCHAR(255))");

         jdbcTemplate.execute("DROP TABLE completedbuilds IF EXISTS");
         jdbcTemplate.execute("CREATE TABLE completedbuilds(" +
                 "id SERIAL, toolbox VARCHAR(255), ctf VARCHAR(255))");
     }


    @Bean
    CommandLineRunner init(StorageService storageService) {
      return (args) -> {
          storageService.deleteAll();
          storageService.init();
      };
    }
  }
