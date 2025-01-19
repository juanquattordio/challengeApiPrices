# E-commerce
# 🛍️ About the API

This API is designed to facilitate the retrieval of product prices in an e-commerce platform. It allows clients to
obtain the updated price of a specific product for a given brand at a specific moment in time.  
It is a Spring-boot REST microservice based on a maven project.

## ⚙️ Requirements

- Java 23.
- JDK 23.
- Maven 3.9.9

## 📚 Technologies

### Common

| Technology                                                                                                                                                          | Purpose                                                                                                                                                                                                                                   |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Hexagonal architecture                                                                                                                                              | I tried to follow an hexagonal clean architecture. The code is divided into application, domain and infrastructure layers. To avoid layer couplings, I created the interfaces as "ports" which have been implementated by the "adapters". |
| [Test Driven Development](https://developer.ibm.com/articles/5-steps-of-test-driven-development/)                                                                   | I tried to apply TDD during the software development life cycle.                                                                                                                                                                          |
| [Domain Driven Design](https://medium.com/@alessandro.traversi/implementing-domain-driven-design-ddd-architecture-a-deep-dive-into-the-domain-feature-cd26aa0c4fc0) | I tried to include some DDD patterns into this project, like Models, ValueObject.                                                                                                                                                         |
| [Lombok](https://projectlombok.org/)                                                                                                                                | Library to create builders, setters, getters, etc.                                                                                                                                                                                        |
| [MapStruct](https://mapstruct.org/)                                                                                                                                 | Library to create mappers to pass objects between the different layers.                                                                                                                                                                   |
| [Spring-boot](https://spring.io/)                                                                                                                                   | Java framework that helps to create a REST microservice easily.                                                                                                                                                                           |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)                                                                                                       | JPA based repositories implementation                                                                                                                                                                                                     |
| [H2Database](https://www.h2database.com/html/main.html)                                                                                                             | Very fast, open source, JDBC API. Support embedded and server modes; disk-based or in-memory databases                                                                                                                                    |
| [JUnit 5](https://junit.org/junit5/)                                                                                                                                | Testing framework.                                                                                                                                                                                                                        |
| [Mockito](https://site.mockito.org/)                                                                                                                                | Mocking framework for testing.                                                                                                                                                                                                            |
| [Swager](https://swagger.io/)                                                                                                                                       | To document and describe the structure of the APIs.                                                                                                                                                                                       |
| [JaCoco](https://www.baeldung.com/jacoco)                                                                                                                           | A code coverage reports generator for Java projects.                                                                                                                                                                                      |

## 🚀 How to execute the application

Go to the project root directory and execute the following command to compile, test, package and install the different
artifacts in your local maven repository.

```shell
mvn clean install
```
After creating all artifacts you can run the project with the following command:

```shell
mvn spring-boot:run
```

When the application is running properly it provides the following endpoints:
- `http://localhost:8080/eccomerce-doc.html`. Swagger interface based on the OpenAPI auto-generated schema that helps you
  to test the endpoints.
- `http://localhost:8080/api-docs`. OpenAPI schema auto-generated from the swagger annotation provided by
    the `springdoc` dependency.
- `http://localhost:8080/h2-console`. The H2 Console application lets you access a database using a browser.

## 📌 How to check code coverage report
To create the coverage report, execute:
```shell
mvn clean test jacoco:report
```

This will create the file `index.html` (`challengeApiPrices\target\site\jacoco`). Open it into the browser.

_Note: all classes whose file name ends with **/*MapperImpl.class, have been excluded from the report._


## 🔨 More comments	
### Database optimization
- I created an index composed by brandId, productId and dateTime to make the query more efficient. In addition, I used an Enum class to store the codes and names of the brands because these values are practically fixed over time. In this way, combined queries to the database are avoided.
- The fields brandId and productId are String because they are codes, I assume that in the future they might include characters. If they ensure only numbers, it could be changed and the database becomes more efficient.
###### Future tasks
- Apply CQRS pattern ([link](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)). Segregates read and write operations for a data store into separate data models. This allows each model to be optimized independently and can improve performance, scalability, and security of an application.
- Create DB partitions. If the table is very large, partitioning the table can help. For example, you can partition it by date range or by brandId.
- Cache implementation. Think that you could implement a cache to respond without overloading the DB, but it would depend a little on the dynamics of loading or updating prices in the DB.