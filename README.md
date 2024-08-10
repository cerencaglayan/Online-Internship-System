# Online Internship System Backend

This repository contains the backend implementation for the Online Internship System, built using Spring Boot. It provides a RESTful API for managing internships, announcements, and users. The project is designed to handle the data operations required for an online internship portal.

## Table of Contents

- [Project Setup](#project-setup)
- [API Endpoints](#api-endpoints)
  - [Announcements](#announcements)
  - [Authentication](#authentication)
  - [Companies](#companies)
  - [Internships](#internships)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Project Setup

| Step | Command/Instruction                                      |
|------|----------------------------------------------------------|
| 1    | Clone the repository                                     |
|      | `git clone https://github.com/yourusername/online-internship-system.git` |
|      | `cd online-internship-system`                            |
| 2    | Build the project (ensure you have Maven installed)      |
|      | `./mvnw clean install`                                   |
| 3    | Run the application                                      |
|      | `./mvnw spring-boot:run`                                 |
| 4    | Access the API documentation                             |
|      | Visit `http://localhost:8080/swagger-ui.html`            |

## API Endpoints

### Announcements

| HTTP Method | Endpoint                      | Description                              |
|-------------|-------------------------------|------------------------------------------|
| GET         | `/api/announcement`           | Retrieve all announcements               |
| GET         | `/api/announcement/{id}`      | Retrieve a specific announcement by ID   |
| POST        | `/api/announcement`           | Create a new announcement                |
| PUT         | `/api/announcement/{id}`      | Update an existing announcement by ID    |
| DELETE      | `/api/announcement/{id}`      | Delete an announcement by ID             |

### Authentication

| HTTP Method | Endpoint             | Description                              |
|-------------|----------------------|------------------------------------------|
| POST        | `/api/auth/login`    | Authenticate a user and return a JWT token |
| POST        | `/api/auth/register` | Register a new user account              |

### Companies

| HTTP Method | Endpoint               | Description                              |
|-------------|------------------------|------------------------------------------|
| GET         | `/api/company`         | Retrieve all companies                   |
| GET         | `/api/company/{id}`    | Retrieve a specific company by ID        |
| POST        | `/api/company`         | Add a new company                        |
| PUT         | `/api/company/{id}`    | Update company details                   |
| DELETE      | `/api/company/{id}`    | Remove a company                         |

### Internships

| HTTP Method | Endpoint                | Description                              |
|-------------|-------------------------|------------------------------------------|
| GET         | `/api/internship`       | Retrieve all internships                 |
| GET         | `/api/internship/{id}`  | Retrieve a specific internship by ID     |
| POST        | `/api/internship`       | Create a new internship                  |
| PUT         | `/api/internship/{id}`  | Update an internship                     |
| DELETE      | `/api/internship/{id}`  | Delete an internship                     |

## Technologies Used

| Technology | Description                                        |
|------------|----------------------------------------------------|
| Spring Boot| Backend framework for building Java applications   |
| Maven      | Dependency management and build automation tool    |
| Hibernate  | ORM framework for database interaction             |
| MySQL      | Database used for storing data                     |

## Contributing

| How to Contribute                                      |
|--------------------------------------------------------|
| Contributions are welcome!                             |
| Please fork the repository and create a pull request.  |

## License

| License Information                                    |
|--------------------------------------------------------|
| This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. |
