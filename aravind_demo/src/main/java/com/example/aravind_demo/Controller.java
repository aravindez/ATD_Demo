package com.example.aravind_demo;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import Customer;

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
	 * @param	firstName first name to filter by
	 * @param	lastName last name to filter by
	 * @param	display boolean value informing the API whether results should be sent as HTML or JSON
	 * @return	HTML or JSON representation of results from DB
	 */
	@GetMapping("/customers")
	public String getCustomers(
			@RequestParam(name="firstName", required=false) String firstName,
			@RequestParam(name="lastName", required=false) String lastName) {
		List<Customer> results;
		if (firstName == null && lastName == null) {
			results = repo.findAll();
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
	 * Adds new Customer to DB
	 * 
	 * @param	newCustomer Customer object describing new Customer
	 */
	@PostMapping("/customers")
	public ResponseEntity<Object> postCustomers(@RequestBody Customer newCustomer) {
		Customer badRequest = new Customer();
		if (newCustomer.equals(badRequest)) {
			System.out.println("\nBAD REQUEST");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} else {
			repo.save(newCustomer);
			return new ResponseEntity<Object>(this.gson.toJson(newCustomer), HttpStatus.OK);
		}
	}
	
	//TODO: Look into best practices about using POST for both CREATE and UPDATE since both can be handled using save()
	
	/**
	 * PUT Request for "/customers"
	 * Updates existing Customer
	 * 
	 * @param	updatedCustomer
	 */
	@PutMapping("/customers")
	public ResponseEntity<Object> putCustomers(@RequestBody Customer updatedCustomer) {
		Customer badRequest = new Customer();
		if (updatedCustomer.equals(badRequest)) {
			System.out.println("\nBAD REQUEST");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} else {
			repo.save(updatedCustomer);
			return new ResponseEntity<Object>(this.gson.toJson(updatedCustomer), HttpStatus.BAD_REQUEST);
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
