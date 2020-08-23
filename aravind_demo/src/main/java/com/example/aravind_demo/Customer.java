package com.example.aravind_demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;

import lombok.Getter;
//import lombok.Setter;

/**
 * Customer Entity
 * Holds simple information on a test customer: id, first name, and last name.
 * 
 * @author aravindez
 *
 */
@Document(collection = "customers")
public class Customer {

	@Id @Getter
	private String id;
	
	@Getter
	private String firstName;
	@Getter
	private String lastName;
	
	/**
	 * Default Constructor
	 * Populates Customer with first name, NA, and last name, NA.
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
		if (this.id == null && that.id == null) {
			if (that.firstName.equals(this.firstName) && that.lastName.equals(this.lastName)) {
				return true;
			}
		} else if (this.id == null || that.id == null) { return false; }
		else {
			if (that.id.equals(this.id) && that.firstName.equals(this.firstName) && that.lastName.equals(this.lastName)) {
				return true;
			}
		}
		return false;
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}