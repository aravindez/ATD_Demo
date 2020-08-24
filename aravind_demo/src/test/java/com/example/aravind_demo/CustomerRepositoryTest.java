package com.example.aravind_demo;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

//@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)
@DataMongoTest
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles(profiles = {"test"})
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository repo;
	
	private List<Customer> testCustomers;
	
	/**
	 * Constructor
	 * Create an ArrayList of test customers
	 */
	public CustomerRepositoryTest() {
		this.testCustomers = new ArrayList<Customer>();
		this.testCustomers.add(new Customer("testFirst1", "testLast1"));
		this.testCustomers.add(new Customer("testFirst1", "testLast2"));
		this.testCustomers.add(new Customer("testFirst2", "testLast1"));
	}
	
	/**
	 * Test saving customers
	 */
	@Test
	@Order(1)
	public void testSaveCustomer() throws Exception {
		this.repo.save(testCustomers.get(0));
		this.repo.save(testCustomers.get(1));
		this.repo.save(testCustomers.get(2));

		List<Customer> savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(0).getFirstName(), testCustomers.get(0).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(0).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(0).getLastName(), savedCustomers.get(0).getLastName());

		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(1).getFirstName(), testCustomers.get(1).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(1).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(1).getLastName(), savedCustomers.get(0).getLastName());

		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(2).getFirstName(), testCustomers.get(2).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(2).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(2).getLastName(), savedCustomers.get(0).getLastName());
	}
	
	/**
	 * test findAll customers
	 */
	@Test
	@Order(2)
	public void testFindAllCustomers() throws Exception {
		List<Customer> savedCustomers = this.repo.findAll();
		assertEquals(3, savedCustomers.size());

		assertEquals(testCustomers.get(0).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(0).getLastName(), savedCustomers.get(0).getLastName());
		assertEquals(testCustomers.get(1).getFirstName(), savedCustomers.get(1).getFirstName());
		assertEquals(testCustomers.get(1).getLastName(), savedCustomers.get(1).getLastName());
		assertEquals(testCustomers.get(2).getFirstName(), savedCustomers.get(2).getFirstName());
		assertEquals(testCustomers.get(2).getLastName(), savedCustomers.get(2).getLastName());
	}
	
	/**
	 * test finding customers by id
	 * @throws Exception
	 */
	@Test @Order(3)
	public void testFindById() throws Exception {
		List<Customer> savedCustomers = this.repo.findAll();
		
		Optional<Customer> savedCustomer = this.repo.findById(savedCustomers.get(0).getId());
		assertEquals(true, savedCustomer.isPresent());
		
		assertEquals(savedCustomers.get(0).getId(), savedCustomer.get().getId());
		assertEquals(savedCustomers.get(0).getFirstName(), savedCustomer.get().getFirstName());
		assertEquals(savedCustomers.get(0).getLastName(), savedCustomer.get().getLastName());
		
		savedCustomer = this.repo.findById(savedCustomers.get(1).getId());
		assertEquals(true, savedCustomer.isPresent());
		
		assertEquals(savedCustomers.get(1).getId(), savedCustomer.get().getId());
		assertEquals(savedCustomers.get(1).getFirstName(), savedCustomer.get().getFirstName());
		assertEquals(savedCustomers.get(1).getLastName(), savedCustomer.get().getLastName());
		
		savedCustomer = this.repo.findById(savedCustomers.get(2).getId());
		assertEquals(true, savedCustomer.isPresent());
		
		assertEquals(savedCustomers.get(2).getId(), savedCustomer.get().getId());
		assertEquals(savedCustomers.get(2).getFirstName(), savedCustomer.get().getFirstName());
		assertEquals(savedCustomers.get(2).getLastName(), savedCustomer.get().getLastName());
	}
	
	/**
	 * test finding customers by first name
	 */
	@Test
	@Order(4)
	public void testFindByFirstName() throws Exception {
		List<Customer> savedCustomers = this.repo.findByFirstName("testFirst1");
		assertEquals(2, savedCustomers.size());

		assertEquals(testCustomers.get(0).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(0).getLastName(), savedCustomers.get(0).getLastName());
		assertEquals(testCustomers.get(1).getFirstName(), savedCustomers.get(1).getFirstName());
		assertEquals(testCustomers.get(1).getLastName(), savedCustomers.get(1).getLastName());
		
		savedCustomers = this.repo.findByFirstName("testFirst2");
		assertEquals(1, savedCustomers.size());

		assertEquals(testCustomers.get(2).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(2).getLastName(), savedCustomers.get(0).getLastName());
	}
	
	/**
	 * test finding customers by last name
	 */
	@Test
	@Order(5)
	public void testFindByLastName() throws Exception {
		List<Customer> savedCustomers = this.repo.findByLastName("testLast1");
		assertEquals(2, savedCustomers.size());

		assertEquals(testCustomers.get(0).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(0).getLastName(), savedCustomers.get(0).getLastName());
		assertEquals(testCustomers.get(2).getFirstName(), savedCustomers.get(1).getFirstName());
		assertEquals(testCustomers.get(2).getLastName(), savedCustomers.get(1).getLastName());
		
		savedCustomers = this.repo.findByLastName("testLast2");
		assertEquals(1, savedCustomers.size());

		assertEquals(testCustomers.get(1).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(1).getLastName(), savedCustomers.get(0).getLastName());
	}
	
	/**
	 * test finding customers by first and last name
	 */
	@Test
	@Order(6)
	public void testFindByFirstNameAndLastName() throws Exception {
		List<Customer> savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(0).getFirstName(), testCustomers.get(0).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(0).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(0).getLastName(), savedCustomers.get(0).getLastName());
		
		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(1).getFirstName(), testCustomers.get(1).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(1).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(1).getLastName(), savedCustomers.get(0).getLastName());
		
		savedCustomers = this.repo.findByFirstNameAndLastName(testCustomers.get(2).getFirstName(), testCustomers.get(2).getLastName());
		assertEquals(1, savedCustomers.size());
		assertEquals(testCustomers.get(2).getFirstName(), savedCustomers.get(0).getFirstName());
		assertEquals(testCustomers.get(2).getLastName(), savedCustomers.get(0).getLastName());
	}
	
	@Test
	@Order(7)
	public void testUpdateCustomer() throws Exception {
		this.testCustomers = this.repo.findAll();
		Customer updatedCustomer = new Customer(this.testCustomers.get(0).getId(), "testFirst3", "testLast3");
		this.repo.save(updatedCustomer);

		Optional<Customer> savedCustomer = this.repo.findById(this.testCustomers.get(0).getId());
		assertEquals(true, savedCustomer.isPresent());
		assertEquals(updatedCustomer, savedCustomer.get());
	}
	
	@Test
	@Order(8)
	public void testDeleteCustomer() throws Exception {
		this.testCustomers = this.repo.findAll();
		Optional<Customer> savedCustomer;
		for (Customer c : this.testCustomers) {
			this.repo.deleteById(c.getId());
			savedCustomer = repo.findById(c.getId());
			assertEquals(false, savedCustomer.isPresent());
		}
		List<Customer> savedCustomers = this.repo.findAll();
		assertEquals(0, savedCustomers.size());
	}

}
