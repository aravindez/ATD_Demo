package com.example.aravind_demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@WebFluxTest
@ActiveProfiles(profiles = {"test"})
class ControllerTests {
	
	@MockBean
	private CustomerRepository repo;
	
	private final WebTestClient webTestClient;
	
	private final String uri = "http://localhost:3001/customers";
	
	public ControllerTests(@Autowired WebTestClient wtc) {
		this.webTestClient = wtc;
	}
	
	/**
	 * GET -  all customers
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
	 * GET - customer by id
	 * @throws Exception
	 */
	@Test
	public void getCustomerById() throws Exception {
		Customer testCustomer = new Customer("asdf", "John", "Smith");
		
		Mockito.when(repo.findById("asdf")).thenReturn(Optional.of(testCustomer));
		Mockito.when(repo.findById("fdsa")).thenReturn(Optional.empty());
		
		this.webTestClient
			.get()
			.uri(this.uri + "?id=asdf")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.[0].id").isEqualTo("asdf")
			.jsonPath("$.[0].firstName").isEqualTo("John")
			.jsonPath("$.[0].lastName").isEqualTo("Smith");
		
		this.webTestClient
			.get()
			.uri(this.uri + "?id=fdsa")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$").isEmpty();
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

	/**
	 * GET - 2 sets of customers by last name
	 * @throws Exception
	 */
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

	/**
	 * GET - each customer by first and last name
	 * @throws Exception
	 */
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
	
	/**
	 * POST - save new customer to DB
	 * @throws Exception
	 */
	@Test
	public void postCustomers() throws Exception {
		Customer badCustomer = new Customer();
		Customer customer = new Customer("John", "Smith");
		
		//Mockito.when(this.repo.save(badCustomer)).thenReturn(badCustomer);
		//Mockito.when(this.repo.save(customer)).thenReturn(customer);
		
		this.webTestClient
			.post()
			.uri(this.uri)
			.exchange()
			.expectStatus().isBadRequest();
		
		this.webTestClient
			.post()
			.uri(this.uri)
			.body(Mono.just(badCustomer), Customer.class)
			.exchange()
			.expectStatus().isBadRequest();
		
		this.webTestClient
			.post()
			.uri(this.uri)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(customer), Customer.class)
			.exchange()
			.expectStatus().isOk();
	}

	/**
	 * PUT - update existing customer in DB
	 * @throws Exception
	 */
	@Test
	public void putCustomers() throws Exception {
		Customer badCustomer = new Customer();
		Customer dneCustomer = new Customer("1234asdf", "John", "Smith");
		Customer existingCustomer = new Customer("1a2s3d4f", "John", "Smith");

		//Mockito.when(this.repo.save(badCustomer)).thenReturn(badCustomer);
		//Mockito.when(this.repo.save(existingCustomer)).thenReturn(customer);
		
		Mockito.when(this.repo.findById(dneCustomer.getId())).thenReturn(Optional.empty());
		Mockito.when(this.repo.findById(existingCustomer.getId())).thenReturn(Optional.of(existingCustomer));
		
		this.webTestClient
			.put()
			.uri(this.uri)
			.exchange()
			.expectStatus().isBadRequest();
		
		this.webTestClient
			.put()
			.uri(this.uri)
			.body(Mono.just(badCustomer), Customer.class)
			.exchange()
			.expectStatus().isBadRequest();
		
		this.webTestClient
			.put()
			.uri(this.uri)
			.body(Mono.just(dneCustomer), Customer.class)
			.exchange()
			.expectStatus().isBadRequest();
		
		this.webTestClient
			.put()
			.uri(this.uri)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(existingCustomer), Customer.class)
			.exchange()
			.expectStatus().isOk();
	}
	
	/**
	 * DELETE - deletes customer with the given id
	 * @throws Exception
	 */
	@Test
	public void deleteCustomers() throws Exception {
		this.webTestClient
			.delete()
			.uri(this.uri)
			.exchange()
			.expectStatus().isBadRequest();

		this.webTestClient
			.delete()
			.uri(this.uri + "?id=asdf")
			.exchange()
			.expectStatus().isOk();
	}

}
