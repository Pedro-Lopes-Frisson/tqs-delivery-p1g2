package ua.tqs.frostini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class FrostiniApplication {
  
  public static void main( String[] args ) {
    SpringApplication.run( FrostiniApplication.class, args );
  }
  
}
