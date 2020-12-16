package com.rock.jpetstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.rock.jpetstore.domain.HealthCheck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LiveController {

	@Value("${spring.application.name}")
	private String appName;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
	@RequestMapping(method = RequestMethod.GET, path = "/")
	public class VideoNotFoundException extends RuntimeException {
	}
	
	//@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, path = "/live")
	public @ResponseBody ResponseEntity<Object> live() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Retry-After", "3600");

		Map<String, List<HealthCheck>> map = new HashMap<String, List<HealthCheck>>();
		List<HealthCheck> healthChecks = new ArrayList<HealthCheck>();
		Date dateNow = Calendar.getInstance().getTime();

		HealthCheck app = new HealthCheck(appName, "OK", dateNow);
		HealthCheck database = new HealthCheck(appName + "-db", "OK", dateNow);

		try {
			jdbcTemplate.execute("SELECT 1");
		} catch (Exception e) {
			database.setStatus("err");
		}

		healthChecks.add(app);
		healthChecks.add(database);

	    map.put("health", healthChecks);
	       
		return new ResponseEntity<>(map, headers, HttpStatus.OK);
	}
}