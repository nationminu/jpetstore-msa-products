package com.rock.jpetstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rock.jpetstore.domain.Products;
import com.rock.jpetstore.service.ProductService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	// @ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> all() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		List<Products> products = productService.getAll();

		if (products == null) {
			return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(products, headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/products/{productid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> get(@PathVariable String productid) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		Products products = productService.getById(productid);

		if (products == null) {
			// return new ResponseEntity<>("{}", headers, HttpStatus.OK);
			return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(products, headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/products/{productid}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<Object, Object>> delete(@PathVariable String productid) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		Map<Object, Object> map = new HashMap<>();

		try {
			productService.delete(productid);

			map.put("result", "OK");
		} catch (Exception e) {
			map.put("result", "NOK");
		}

		return new ResponseEntity<>(map, headers, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/products/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Map<Object, Object>> insert(@RequestBody Products products) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		Map<Object, Object> map = new HashMap<>();

		try {
			productService.save(products);

			map.put("result", "OK");
		} catch (Exception e) {
			map.put("result", "NOK");
			return new ResponseEntity<>(map, headers, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(map, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/products/{productid}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Map<Object, Object>> update(@PathVariable String productid, @RequestBody Products products) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		Map<Object, Object> map = new HashMap<>();

		try {
			productService.save(products);

			map.put("result", "OK");
		} catch (Exception e) {
			map.put("result", "NOK");
			return new ResponseEntity<>(map, headers, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(map, headers, HttpStatus.CREATED);
	}
}
