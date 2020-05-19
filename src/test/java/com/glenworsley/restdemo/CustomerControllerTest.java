package com.glenworsley.restdemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getAllCallsRepositoryFindAll() {
        customerController.all();
        verify(customerRepository).findAll();
    }

    @Test
    void newCustomerIsSavedToRepository() {
        Customer customer = new Customer();
        customerController.newCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void newCustomerReturnsSavedCustomerDetails() {
        Customer customer = new Customer();
        Customer savedCustomer = new Customer();
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Customer returnedCustomer = customerController.newCustomer(customer);
        assertThat(returnedCustomer.equals(savedCustomer));
    }

    @Test
    void getCustomerReturnsCustomer() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer returnedCustomer = customerController.getCustomer(1L);
        assertThat(returnedCustomer.equals(customer));
    }

    @Test()
    void getCustomerThrowsCustomerNotFoundExceptionForUnknownCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomer(1L); });
    }

    @Test
    void replaceCustomerPassesCorrectDetailsToRepository() {
        Customer customer = createTestCustomer(1L, "bob", "down");
        Customer updatedCustomer = createTestCustomer(1L, "stan", "dup");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());
        customerController.replaceCustomer(customer, 1L);
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getFirstName().equals("stan"));
        assertThat(argumentCaptor.getValue().getLastName().equals("dup"));
    }

    private Customer createTestCustomer(long id, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        return customer;
    }

    @Test
    void replaceCustomerWithUnknownIdThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.replaceCustomer(new Customer(), 1L);
        });
    }

    @Test
    void deleteCustomerCallsRepositoryDeleteByIdWithCorrectId() {
        customerController.deleteCustomer(1L);
        verify(customerRepository).deleteById(eq(1L));
    }
}