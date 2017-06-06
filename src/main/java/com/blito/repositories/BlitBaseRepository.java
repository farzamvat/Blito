package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.blito.models.Blit;

@NoRepositoryBean
public interface BlitBaseRepository <T extends Blit> extends JpaRepository<T,Long> {
	
}
