package br.com.casadocodigo.loja.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.casadocodigo.loja.models.Product;

@org.springframework.stereotype.Repository
public interface UserDAO extends Repository<Product,Integer>{
	
	@Query( "select u	from User u	where u.login =	:login")
	public UserDetails loadUserByUsername(@Param("login") String username);

}
