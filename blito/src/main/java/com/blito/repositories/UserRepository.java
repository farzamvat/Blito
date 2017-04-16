package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.User;

public interface UserRepository extends JpaRepository<User,Long> {

}
