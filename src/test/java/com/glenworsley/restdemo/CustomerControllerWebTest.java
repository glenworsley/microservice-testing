package com.glenworsley.restdemo;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.glenworsley.restdemo.CustomerFixture.createCustomer;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Purpose of this test is to test the Spring configuration at the Web MVC layer.
 */
@WebMvcTest
public class CustomerControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void getCustomersReturnsCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(createCustomer(1L,"bob", "down"),
                createCustomer(2L, "stan", "dup"));
        when(customerRepository.findAll()).thenReturn(customers);
        this.mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void getSingleCustomerReturnsCustomer() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(createCustomer(1L, "bob", "down")));
        this.mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("bob"))
                .andExpect(jsonPath("$.lastName").value("down"));
    }

    @Test
    public void createNewCustomer() throws Exception {
        when(customerRepository.save(ArgumentMatchers.any())).thenReturn(createCustomer(5L, "bob", "down"));
        this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"bob\", \"lastName\": \"down\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(equalTo(5))))
                .andExpect(jsonPath("$.firstName").value(is(equalTo("bob"))))
                .andExpect(jsonPath("$.lastName").value(is(equalTo("down"))));
    }

    @Test
    public void getCustomerNotFound() throws Exception {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(is(equalTo("Could not find customer 99"))));
    }

    @Test
    public void replaceCustomer() throws Exception {
        when(customerRepository.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.of(createCustomer(99L, "bob", "down")));
        when(customerRepository.save(ArgumentMatchers.any())).thenReturn(createCustomer(99L, "stan", "down"));
        this.mockMvc.perform(put("/customers/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 99, \"firstName\": \"stan\", \"lastName\": \"down\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("stan"));
        verify(customerRepository).findById(99L);
    }

    @Test
    public void replaceCustomerNotFound() throws Exception {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/customers/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"bob\", \"lastName\": \"down\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(is(equalTo("Could not find customer 99"))));
    }

    @Test
    public void deleteCustomer() throws Exception {
        this.mockMvc.perform(delete("/customers/99"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(customerRepository).deleteById(99L);
    }
}
