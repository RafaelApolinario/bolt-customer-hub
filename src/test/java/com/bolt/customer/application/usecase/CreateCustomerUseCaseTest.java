package com.bolt.customer.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bolt.customer.application.command.ConsumerUnitCommand;
import com.bolt.customer.application.command.CreateCustomerCommand;
import com.bolt.customer.application.event.CustomerMgAnalysisPublisher;
import com.bolt.customer.application.event.CustomerRegisteredInMgEvent;
import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerDomainService;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.CustomerStatus;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.BusinessException;
import com.bolt.customer.domain.exception.ConflictException;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

	private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressGateway addressGateway;

	@Mock
	private CustomerMgAnalysisPublisher mgAnalysisPublisher;

	private CreateCustomerUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new CreateCustomerUseCase(customerRepository, addressGateway, new CustomerDomainService(), mgAnalysisPublisher, CLOCK);
	}

	@Test
	void shouldCreateCustomerWhenDataIsValid() {
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));
		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Customer customer = useCase.execute(command("12345678901", "UC-100", "30140071"));

		assertThat(customer.getName()).isEqualTo("Maria Silva");
		assertThat(customer.getDocument()).isEqualTo(Document.of("12345678901"));
		assertThat(customer.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
		assertThat(customer.getConsumerUnits()).hasSize(1);
		assertThat(customer.getCreatedAt()).isEqualTo(Instant.parse("2026-05-20T10:00:00Z"));
		verify(customerRepository).save(any(Customer.class));
	}

	@Test
	void shouldPublishMgAnalysisEventWhenCustomerHasConsumerUnitInMg() {
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));
		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Customer customer = useCase.execute(command("12345678901", "UC-100", "30140071"));

		verify(mgAnalysisPublisher).publish(CustomerRegisteredInMgEvent.of(
				customer.getId().toString(),
				"12345678901",
				Instant.parse("2026-05-20T10:00:00Z")));
	}

	@Test
	void shouldNotPublishMgAnalysisEventWhenCustomerHasNoConsumerUnitInMg() {
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("RJ"));
		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		useCase.execute(command("12345678901", "UC-100", "30140071"));

		verify(mgAnalysisPublisher, never()).publish(any());
	}

	@Test
	void shouldNotCreateCustomerWhenDocumentAlreadyExists() {
		when(customerRepository.existsByDocument(Document.of("12345678901"))).thenReturn(true);

		assertThatThrownBy(() -> useCase.execute(command("12345678901", "UC-100", "30140071")))
				.isInstanceOf(ConflictException.class)
				.hasMessage("Document already exists");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	void shouldNotCreateCustomerWhenConsumerUnitBelongsToAnotherCustomer() {
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));
		when(customerRepository.existsByConsumerUnitNumber("UC-100")).thenReturn(true);

		assertThatThrownBy(() -> useCase.execute(command("12345678901", "UC-100", "30140071")))
				.isInstanceOf(ConflictException.class)
				.hasMessage("Consumer unit already belongs to another customer");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	void shouldNotCreateCustomerWhenConsumerUnitIsDuplicatedInRequest() {
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));

		CreateCustomerCommand command = new CreateCustomerCommand(
				"Maria Silva",
				"12345678901",
				List.of(
						new ConsumerUnitCommand("UC-100", "30140071"),
						new ConsumerUnitCommand("UC-100", "30140071")));

		assertThatThrownBy(() -> useCase.execute(command))
				.isInstanceOf(ConflictException.class)
				.hasMessage("Consumer unit appears more than once in request");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	void shouldNotCreateCustomerWhenConsumerUnitIsInBlockedState() {
		when(addressGateway.findByZipCode("01001000")).thenReturn(address("SP"));

		assertThatThrownBy(() -> useCase.execute(command("12345678901", "UC-100", "01001000")))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Consumer units in SP, RS or PR are not allowed");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	void shouldReturnErrorWhenZipCodeIsInvalid() {
		when(addressGateway.findByZipCode("99999999"))
				.thenThrow(new BusinessException("address.zip-code.not-found", "Zip code not found in ViaCEP"));

		assertThatThrownBy(() -> useCase.execute(command("12345678901", "UC-100", "99999999")))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Zip code not found in ViaCEP");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	private static CreateCustomerCommand command(String document, String consumerUnitNumber, String zipCode) {
		return new CreateCustomerCommand(
				"Maria Silva",
				document,
				List.of(new ConsumerUnitCommand(consumerUnitNumber, zipCode)));
	}

	private static Address address(String state) {
		return new Address("30140071", "Rua A", "Centro", "Belo Horizonte", state);
	}
}
