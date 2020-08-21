package com.example.aravind_demo;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles(profiles = {"test"})
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository repo;
	
	private List<Customer> testCustomers;
	
	public CustomerRepositoryTest() {
		this.testCustomers = new ArrayList<Customer>();
		this.testCustomers.add(new Customer("testFirst1", "testLast1"));
		this.testCustomers.add(new Customer("testFirst1", "testLast2"));
		this.testCustomers.add(new Customer("testFirst2", "testLast1"));
	}
	
	@Test
	@Order(1)
	public void testSaveCustomer() {
		this.repo.save(testCustomers.get(0));
		this.repo.save(testCustomers.get(1));
		this.repo.save(testCustomers.get(2));

		List<Customer> savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(0).firstName, testCustomers.get(0).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(0).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(0).lastName, savedCustomers.get(0).lastName);

		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(1).firstName, testCustomers.get(1).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(1).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(1).lastName, savedCustomers.get(0).lastName);

		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(2).firstName, testCustomers.get(2).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(2).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(2).lastName, savedCustomers.get(0).lastName);
	}
	
	@Test
	@Order(2)
	public void testFindAllCustomers() {
		List<Customer> savedCustomers = this.repo.findAll();
		System.out.println(savedCustomers);
		assertEquals(3, savedCustomers.size());

		assertEquals(testCustomers.get(0).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(0).lastName, savedCustomers.get(0).lastName);
		assertEquals(testCustomers.get(1).firstName, savedCustomers.get(1).firstName);
		assertEquals(testCustomers.get(1).lastName, savedCustomers.get(1).lastName);
		assertEquals(testCustomers.get(2).firstName, savedCustomers.get(2).firstName);
		assertEquals(testCustomers.get(2).lastName, savedCustomers.get(2).lastName);
	}
	
	@Test
	@Order(3)
	public void testFindByFirstName() {
		List<Customer> savedCustomers = this.repo.findByFirstName("testFirst1");
		assertEquals(2, savedCustomers.size());

		assertEquals(testCustomers.get(0).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(0).lastName, savedCustomers.get(0).lastName);
		assertEquals(testCustomers.get(1).firstName, savedCustomers.get(1).firstName);
		assertEquals(testCustomers.get(1).lastName, savedCustomers.get(1).lastName);
		
		savedCustomers = this.repo.findByFirstName("testFirst2");
		assertEquals(1, savedCustomers.size());

		assertEquals(testCustomers.get(2).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(2).lastName, savedCustomers.get(0).lastName);
	}
	
	@Test
	@Order(4)
	public void testFindByLastName() {
		List<Customer> savedCustomers = this.repo.findByLastName("testLast1");
		assertEquals(2, savedCustomers.size());

		assertEquals(testCustomers.get(0).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(0).lastName, savedCustomers.get(0).lastName);
		assertEquals(testCustomers.get(2).firstName, savedCustomers.get(1).firstName);
		assertEquals(testCustomers.get(2).lastName, savedCustomers.get(1).lastName);
		
		savedCustomers = this.repo.findByLastName("testLast2");
		assertEquals(1, savedCustomers.size());

		assertEquals(testCustomers.get(1).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(1).lastName, savedCustomers.get(0).lastName);
	}
	
	@Test
	@Order(5)
	public void testFindByFirstNameAndLastName() {
		List<Customer> savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(0).firstName, testCustomers.get(0).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(0).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(0).lastName, savedCustomers.get(0).lastName);
		
		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(1).firstName, testCustomers.get(1).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(1).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(1).lastName, savedCustomers.get(0).lastName);
		
		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(2).firstName, testCustomers.get(2).lastName);
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(2).firstName, savedCustomers.get(0).firstName);
		assertEquals(testCustomers.get(2).lastName, savedCustomers.get(0).lastName);
	}
	
	@Test
	@Order(6)
	public void testUpdateCustomer() {
		this.testCustomers = this.repo.findAll();
		Customer updatedCustomer = new Customer(this.testCustomers.get(0).id, "testFirst3", "testLast3");
		this.repo.save(updatedCustomer);

		Optional<Customer> savedCustomer = this.repo.findById(this.testCustomers.get(0).id);
		assertEquals(true, savedCustomer.isPresent());
		assertEquals(updatedCustomer.id, savedCustomer.get().id);
		assertEquals(updatedCustomer.firstName, savedCustomer.get().firstName);
		assertEquals(updatedCustomer.lastName, savedCustomer.get().lastName);
	}
	
	@Test
	@Order(7)
	public void testDeleteCustomer() {
		this.testCustomers = this.repo.findAll();
		Optional<Customer> savedCustomer;
		for (Customer c : this.testCustomers) {
			this.repo.deleteById(c.id);
			savedCustomer = repo.findById(c.id);
			assertEquals(false, savedCustomer.isPresent());
		}
		List<Customer> savedCustomers = this.repo.findAll();
		assertEquals(0, savedCustomers.size());
	}

}
