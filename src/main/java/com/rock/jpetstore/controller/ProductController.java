package com.rock.jpetstore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity; 
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rock.jpetstore.domain.Items;
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
	
	@Value("${items.server.url}")
	private String itemServer;

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

	@RequestMapping(value = "/products/{productid}/items", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> get(@PathVariable String productid) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

//		Products products = productService.getById(productid);

  		CloseableHttpClient httpClient = HttpClients.createDefault();

        
		try {
 		    //HttpGet request = new HttpGet("http://127.0.0.1:8082/items");
			HttpGet request = new HttpGet(itemServer);

		    request.addHeader("api-key", "change-key");
	        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
	  
	        
	        CloseableHttpResponse response = httpClient.execute(request);
	        
	        try {

                // Get HttpResponse Status
                //System.out.println(response.getProtocolVersion());              // HTTP/1.1
                //System.out.println(response.getStatusLine().getStatusCode());   // 200
                //System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                //System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity(); 
	                
	            if (entity != null) {

	                String result = EntityUtils.toString(entity);
	                //System.out.println(result); 
	                
	            	ObjectMapper mapper = new ObjectMapper(); 
	            	Items[] items = mapper.readValue(result, Items[].class);
	            	ArrayList<Items> new_items = new ArrayList<Items>();
	            	for (Items i : items) {
	            		if(i.getProductId().equals(productid)) {
	            			//System.out.println(i);
	            			new_items.add(i);
	            		}
	                }

	                //System.out.println(new_items);
	            	
	                // return it as a String
	    			return new ResponseEntity<>(new_items, headers, HttpStatus.OK);
	                
	            }
	        } finally {
                response.close();
            }
		} finally {
			httpClient.close();
        }


     
		// return new ResponseEntity<>("{}", headers, HttpStatus.OK);
		return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
		 
	}
	

	@RequestMapping(value = "/products/{productid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> get_items(@PathVariable String productid) throws Exception {

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
			map.put("productid", productid);
			return new ResponseEntity<>(map, headers, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			map.put("result", "NOK");
			return new ResponseEntity<>(map, headers, HttpStatus.NO_CONTENT);
		} 
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
			map.put("products", products);
			return new ResponseEntity<>(map, headers, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("result", "NOK");
			return new ResponseEntity<>(map, headers, HttpStatus.BAD_REQUEST);
		} 
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
			map.put("products", products);
			return new ResponseEntity<>(map, headers, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("result", "NOK");
			return new ResponseEntity<>(map, headers, HttpStatus.BAD_REQUEST);
		} 
	}
}
