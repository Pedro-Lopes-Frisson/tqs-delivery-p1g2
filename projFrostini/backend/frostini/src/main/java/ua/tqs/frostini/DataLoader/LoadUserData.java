package ua.tqs.frostini.DataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import ua.tqs.frostini.repositories.UserRepository;

public class LoadUserData implements ApplicationRunner {
  @Autowired UserRepository userRepository;
  @Override public void run( ApplicationArguments args ) throws Exception {
    //Insert Admin Data
  }
}
