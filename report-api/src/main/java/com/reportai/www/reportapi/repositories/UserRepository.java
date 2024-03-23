package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
