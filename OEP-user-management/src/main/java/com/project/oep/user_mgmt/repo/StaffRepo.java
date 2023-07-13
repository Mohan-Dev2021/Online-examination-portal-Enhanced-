package com.project.oep.user_mgmt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.project.oep.models.Staff;

@EnableJpaRepositories
public interface StaffRepo extends JpaRepository<Staff, Long> {

	Optional<Staff> findByEmail(String email);

}
