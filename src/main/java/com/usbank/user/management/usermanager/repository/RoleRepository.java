package com.usbank.user.management.usermanager.repository;

import com.usbank.user.management.usermanager.model.UserRole;
import com.usbank.user.management.usermanager.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Optional<Role> findByName(UserRole userRole);
}
