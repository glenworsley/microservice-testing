# REST Demo

Project to practice writing tests for a REST style microservice written
using SpringBoot.  It uses Spring Data JPA for Data layer interactions with an in-memory H2 database.

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.glenworsley.restdemo.RestdemoApplication` class from your IDE.

- Download the zip or clone the Git repository.
- Unzip the zip file (if you downloaded one)
- Open Command Prompt and Change directory (cd) to folder containing pom.xml
- Open Eclipse 
   - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
   - Select the project
- Choose the Spring Boot Application file (search for @SpringBootApplication)
- Right Click on the file and Run as Java Application

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

### URLs

You can use cURL or Postman to interact with the API.  Below are the URLs:

|  URL |  Method | Remarks |
|----------|--------------|--------------|
|`http://localhost:8080/customers`                           | GET | Returns list of customers|
|`http://localhost:8080/customers`                       | POST | | Creates a new customer
|`http://localhost:8080/customers/{id}`                 | GET | Return specified customer |
|`http://localhost:8080/customers/{id}` | PUT | Updates the given customer with details from JSON request body |
|`http://localhost:8080/customers/{id}`                             | DELETE | Deletes specified customer |

### Tests

There are 3 tests in the Project:
- RestdemoApplicationTests
- CustomerControllerTest
- CustomerControllerWebTest

## Running the tests

```shell
mvn test
```

### Testing approach

RestdemoApplicationTests is a simple test to verify that the application context loads and that the CustomerController bean is instantiated.
Because constructor injection is used for dependencies, this test will fail if any dependencies for the CustomerController cannot be satisfied.

CustomerControllerTest is a Unit test.  It does not use Spring and is quick to execute.  It mocks out the data layer (CustomerRepository) using Mockito
and verifies interactions with the data layer.

CustomerControllerWebTest is an Integration test (of sorts).  It uses Springs MockMvc and tests the Spring configuration at the HTTP (web) layer.

TODO:
```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system
TODO: add Dockerfile


