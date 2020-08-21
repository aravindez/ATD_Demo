package com.example.aravind_demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;

/**
 * Customer Entity
 * Holds simple information on a test customer: id, first name, and last name.
 * 
 * @author aravindez
 *
 */
@Document(collection = "customers")
public class Customer {

	@Id
	public String id;
	
	public String firstName;
	public String lastName;
	
	/**
	 * Default Constructor
	 * Populates Customer with first name, testFirst, and last name, testLast.
	 */
	public Customer() {
		this("NA", "NA");
	}
	
	/**
	 * New Customer Constructor - when entity is not yet saved in DB
	 * @param firstName
	 * @param lastName
	 */
	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * Existing Customer Constructor - when entity is already saved in DB
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public Customer(String id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s, %s", id, firstName, lastName);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Customer)) {
			return false;
		}
		
		Customer that = (Customer) o;
		if (that.id != this.id) {
			System.out.println("ID MISMATCH");
			System.out.println("this.id (" + (this.id instanceof String) + "): " + this.id);
			System.out.println("this.id (" + (this.id instanceof String) + "): " + this.id);
			return false;
		} else if (that.firstName != this.firstName) {
			System.out.println("FIRSTNAME MISMATCH");
			return false;
		} else if (that.lastName != this.lastName) {
			System.out.println("LASTNAME MISMATCH");
			return false;
		} else {
			return true;
		}

		/*
		if (that.id == this.id && that.firstName == this.firstName && that.lastName == this.lastName) {
			return true;
		} else { return false; }
		*/
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}