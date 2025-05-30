package com.app.Volavia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Volavia.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByEmail(String email);
	User findByUsuario(String usuario);

}
