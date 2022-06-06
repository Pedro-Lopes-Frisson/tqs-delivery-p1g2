package ua.tqs.delivera.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.repositories.AdminRepository;
import ua.tqs.delivera.services.AdminService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
  @Mock AdminRepository adminRepository;
  @InjectMocks AdminService adminService;
  
  Admin admin;
  
  @BeforeEach
  void setUp() {
    admin =
      Admin.builder().email( "admin.Alfredo@delivera.pt" ).name( "Alfredo Ferreira" ).password( "safepassword" )
           .build();
  }
  
  
  @Test
  void testWhenLoginWithUsedEmail_thenReturnValidAdmin() {
    when( adminRepository.findByEmail( admin.getEmail() ) ).thenReturn( Optional.ofNullable( admin ) );
    Admin adminFromService = adminService.login( admin.getEmail() );
    assertNotNull( adminFromService );
    assertThat( adminFromService.getPassword() ).isEqualTo( admin.getPassword() );
    assertThat( adminFromService.getEmail() ).isEqualTo( admin.getEmail() );
    assertThat( adminFromService.getName() ).isEqualTo( admin.getName() );
    verify( adminRepository, times( 1 ) ).findByEmail( admin.getEmail() );
  }
  
  @Test
  void testWhenLoginWithUnusedEmail_thenReturnNullAdmin() {
    when( adminRepository.findByEmail( admin.getEmail() ) ).thenReturn( Optional.empty() );
    Admin adminFromService = adminService.login( admin.getEmail() );
    assertNull( adminFromService );
    verify( adminRepository, times( 1 ) ).findByEmail( admin.getEmail() );
  }
  
}
