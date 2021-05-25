package com.rock.jpetstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.rock.jpetstore.domain.Products; 

@RepositoryRestResource(exported = false) 
public interface ProductRepository extends JpaRepository<Products, String> { 
	Products findByProductId(String productid);

	List<Products> findByNameContainingIgnoreCase(String keyword);
}
