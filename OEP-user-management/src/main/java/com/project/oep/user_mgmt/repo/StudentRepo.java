package com.project.oep.user_mgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.project.oep.models.Student;

@EnableJpaRepositories
public interface StudentRepo extends JpaRepository<Student, Long> {

	Student findByUserName(String userName);

}
