package com.example.aravind_demo;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Controller {
	
	@Autowired
	private CustomerRepository repo;
	
	private Gson gson;
	
	public Controller() {
		this.gson = new Gson();
	}
	
	/**
	 * GET Request for "/"
	 * @return Hello World string
	 */
	@GetMapping("/")
	public String helloWorld() {
		return "JDK10/Spring Boot/Maven - Hello World!";
	}

	/**
	 * GET Request for "/customers"
	 * Handles retrieving all customers as well as filtered results based on parameters
	 * 
	 * @param	id			id to search for (takes precedence)
	 * @param	firstName 	first name to filter by
	 * @param	lastName 	last name to filter by
	 * @return	JSON 		representation of results from DB
	 */
	@GetMapping("/customers")
	public String getCustomers(
			@RequestParam(name="id", required=false) String id,
			@RequestParam(name="firstName", required=false) String firstName,
			@RequestParam(name="lastName", required=false) String lastName) {
		List<Customer> results;
		if (id == null && firstName == null && lastName == null) {
			results = repo.findAll();
		} else if (id != null) {
			results = new ArrayList<Customer>();
			Optional<Customer> result = this.repo.findById(id);
			if (result.isPresent()) {
				results.add(result.get());
			}
		} else if (lastName == null) {
			results = repo.findByFirstName(firstName);
		} else if (firstName == null) {
			results = repo.findByLastName(lastName);
		} else {
			results = repo.findByFirstNameAndLastName(firstName, lastName);
		}
		return this.gson.toJson(results);
	}
	
	/**
	 * POST Request for "/customers"
	 * Checks if firstName/lastName was passed by comparing to a default Customer object then adds new Customer to DB
	 * 
	 * @param	newCustomer Customer object describing new Customer
	 */
	@PostMapping("/customers")
	public ResponseEntity<Object> postCustomers(@RequestBody(required=true) Customer newCustomer) {
		// Business Logic in front end can (should?) handle this check, but included it anyway
		if (newCustomer.getId() != null || newCustomer.getFirstName().equals("NA") || newCustomer.getLastName().equals("NA")) {
			return new ResponseEntity<Object>("{\"error\": \"Missing or invalid new customer information.\"}", HttpStatus.BAD_REQUEST);
		} else {
			repo.save(newCustomer);
			return new ResponseEntity<Object>(this.gson.toJson(newCustomer), HttpStatus.OK);
		}
	}
	
	//TODO: Look into best practices about using POST for both CREATE and UPDATE since both can be handled using save()
	
	/**
	 * PUT Request for "/customers"
	 * Checks if firstName/lastName was passed by comparing to a default Customer object then updates existing Customer in DB
	 * 
	 * @param updatedCustomer	Customer object holding updated customer info
	 */
	@PutMapping("/customers")
	public ResponseEntity<Object> putCustomers(@RequestBody(required=true) Customer updatedCustomer) {
		// Business Logic in front end can (should?) handle this check, but included it anyway
		if (updatedCustomer.getFirstName().equals("NA") || updatedCustomer.getLastName().equals("NA")) {
			return new ResponseEntity<Object>("{\"error\": \"Missing or invalid updated customer information.\"}", HttpStatus.BAD_REQUEST);
		} else if (!this.repo.findById(updatedCustomer.getId()).isPresent()) {
			return new ResponseEntity<Object>("{\"error\": \"Customer does not exist in DB.\"}", HttpStatus.BAD_REQUEST);
		} else {
			repo.save(updatedCustomer);
			return new ResponseEntity<Object>(this.gson.toJson(updatedCustomer), HttpStatus.OK);
		}
	}
	
	/**
	 * DELETE Request for "/customers"
	 * Deletes existing customer
	 * 
	 * @param id	existing customer's ID
	 */
	@DeleteMapping("/customers")
	public void deleteCustomers(@RequestParam(name="id", required=true) String id) {
		repo.deleteById(id);
	}
	
}
