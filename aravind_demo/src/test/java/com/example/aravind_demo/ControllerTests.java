package com.example.aravind_demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@WebFluxTest
class ControllerTests {
	
	@MockBean
	private CustomerRepository repo;
	
	private final WebTestClient webTestClient;
	
	private final String uri = "http://localhost:3001/customers";
	
	public ControllerTests(@Autowired WebTestClient wtc) {
		this.webTestClient = wtc;
	}
	
	/**
	 * Test - GET all customers
	 * @throws Exception
	 */
	@Test
	public void getAllCustomers() throws Exception {
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(new Customer("John", "Smith"));
		customers.add(new Customer("John", "Ruisi"));
		customers.add(new Customer("Alice", "Smith"));
		
		Mockito.when(repo.findAll()).thenReturn(customers);

		this.webTestClient
			.get()
			.uri(this.uri)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Smith")
			.jsonPath("$.[1].firstName").isEqualTo("John")
			.jsonPath("$.[1].lastName").isEqualTo("Ruisi")
			.jsonPath("$.[2].firstName").isEqualTo("Alice")
			.jsonPath("$.[2].lastName").isEqualTo("Smith");
	}
	
	/**
	 * GET - 2 sets of customers by first name
	 * @throws Exception
	 */
	@Test
	public void getCustomersByFirstName() throws Exception {
		List<Customer> customersJohn = new ArrayList<Customer>();
		List<Customer> customersAlice = new ArrayList<Customer>();
		customersJohn.add(new Customer("John", "Smith"));
		customersJohn.add(new Customer("John", "Ruisi"));
		customersAlice.add(new Customer("Alice", "Smith"));

		Mockito.when(repo.findByFirstName("John")).thenReturn(customersJohn);
		Mockito.when(repo.findByFirstName("Alice")).thenReturn(customersAlice);

		this.webTestClient
			.get()
			.uri(this.uri + "?firstName=John")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Smith")
			.jsonPath("$.[1].firstName").isEqualTo("John")
			.jsonPath("$.[1].lastName").isEqualTo("Ruisi");

		this.webTestClient
			.get()
			.uri(this.uri + "?firstName=Alice")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("Alice")
			.jsonPath("$.[0].lastName").isEqualTo("Smith");
	}

	@Test
	public void getCustomersByLastName() throws Exception {
		List<Customer> customersSmith = new ArrayList<Customer>();
		customersSmith.add(new Customer("John", "Smith"));
		customersSmith.add(new Customer("Alice", "Smith"));
		List<Customer> customersRuisi = new ArrayList<Customer>();
		customersRuisi.add(new Customer("John", "Ruisi"));

		Mockito.when(repo.findByLastName("Smith")).thenReturn(customersSmith);
		Mockito.when(repo.findByLastName("Ruisi")).thenReturn(customersRuisi);

		this.webTestClient
			.get()
			.uri(this.uri + "?lastName=Smith")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Smith")
			.jsonPath("$.[1].firstName").isEqualTo("Alice")
			.jsonPath("$.[1].lastName").isEqualTo("Smith");

		this.webTestClient
			.get()
			.uri(this.uri + "?lastName=Ruisi")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Ruisi");
	}

	@Test
	public void getCustomersByFirstNameAndLastName() throws Exception {
		List<Customer> johnSmith = new ArrayList<Customer>();
		johnSmith.add(new Customer("John", "Smith"));
		List<Customer> johnRuisi = new ArrayList<Customer>();
		johnRuisi.add(new Customer("John", "Ruisi"));
		List<Customer> aliceSmith = new ArrayList<Customer>();
		aliceSmith.add(new Customer("Alice", "Smith"));

		Mockito.when(repo.findByFirstNameAndLastName("John", "Smith")).thenReturn(johnSmith);
		Mockito.when(repo.findByFirstNameAndLastName("John", "Ruisi")).thenReturn(johnRuisi);
		Mockito.when(repo.findByFirstNameAndLastName("Alice", "Smith")).thenReturn(aliceSmith);

		this.webTestClient
			.get()
			.uri(this.uri + "?firstName=John&lastName=Smith")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Smith");

		this.webTestClient
			.get()
			.uri(this.uri + "?firstName=John&lastName=Ruisi")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Ruisi");

		this.webTestClient
			.get()
			.uri(this.uri + "?firstName=Alice&lastName=Smith")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].firstName").isEqualTo("Alice")
			.jsonPath("$.[0].lastName").isEqualTo("Smith");
	}
	
	@Test
	public void postCustomers() throws Exception {
		Customer customer = new Customer("John", "Smith");
		
		this.webTestClient
			.post()
			.uri(this.uri)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(customer), Customer.class)
			.exchange()
			.expectStatus().isOk();
	}

}
