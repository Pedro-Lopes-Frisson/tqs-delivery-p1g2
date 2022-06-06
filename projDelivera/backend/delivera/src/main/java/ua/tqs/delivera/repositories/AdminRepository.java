package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.tqs.delivera.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}