package com.rock.jpetstore.service;

import com.rock.jpetstore.domain.Products;
import com.rock.jpetstore.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductService {
 
	@Autowired
	private ProductRepository productRepository;

	// Accounts
	public List<Products> getAll() {
		return productRepository.findAll();
	}
	

	public List<Products> findFilterByName(String keyword) {
		return productRepository.findByNameContainingIgnoreCase(keyword);
	}


	public Products getById(String productid) {
		return productRepository.findByProductId(productid);
	}

	public void save(Products product) {
		productRepository.save(product);
	}

	public void delete(String productid) {
		productRepository.deleteById(productid);
	}
}