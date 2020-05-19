package com.glenworsley.restdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    //aggregate end-point
    @GetMapping("/customers")
    List<Customer> all() {
        return customerRepository.findAll();
    }

    @PostMapping("/customers")
    Customer newCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    //single item end-point
    @GetMapping("/customers/{id}")
    Customer getCustomer(@PathVariable Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PutMapping("/customers/{id}")
    Customer replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @DeleteMapping("/customers/{id}")
    void deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }


}
