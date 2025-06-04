# 📘 Blog Application with AI Integration

This is a full-featured **Spring Boot** blog application enhanced with **AI capabilities**. It uses **Maven** as the build automation tool and follows modular, maintainable architecture principles.

---

## 🌳 Repository Branches Overview

This repository is organized into **three separate branches**, each serving a specific purpose:

---

### 🔹 `main`

> 📦 Contains all the **Spring Boot backend code** for the Blog Application.
> 🔗 [Access the `main` branch](https://github.com/MrPal28/Blog-Application-With-AI-Integration)

---

### 🔹 `ai_api`

> 🤖 Dedicated to the **AI-powered API** that handles intelligent blog post suggestions and processing.
> 🔗 [Access the `ai_api` branch](https://github.com/MrPal28/Blog-Application-With-AI-Integration/tree/ai_api)

---

### 🔹 `Front-end-react`

> 🎨 Contains the complete **React-based front-end** code for the blog application interface.
> 🔗 [Access the `Front-end-react` branch](https://github.com/MrPal28/Blog-Application-With-AI-Integration/tree/Front-end-react)

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot
- Maven
- Lombok
- REST APIs
- MongoDB/MySQL (In the future, we will decide which database technology to use)
- React

---

## 🚀 Getting Started

### 🧰 Prerequisites

- Java 17 or higher
- Maven 3.5.0
- IDE (IntelliJ IDEA Unlimited, IntelliJ IDEA Community Edition, VSCode)
- Git

---

## ⚙️ Build & Run the Application

```bash
# Clone the repository
git clone https://github.com/MrPal28/Blog-Application-With-AI-Integration.git
```

```bash
# Navigate to the project folder
cd Blog-Application-With-AI-Integration
```

```bash
# Build the project using Maven
mvn clean install
```

```bash
# Run the Spring Boot application
mvn spring-boot:run
```

---

## 🔗 Base API URL

```
http://localhost:8082/app/
```

> Use this as the root endpoint for all your API calls.

---

## 📦 Maven Dependencies

Here are the core dependencies currently used in the project:

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!--use for create connection mongodb database-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    
    <!--use for provide security of our spring boot application-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!--use for sending mails to users-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>


    <!-- Lombok for reducing boilerplate code -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 📄 Project Notes

- This `README.md` file **will be updated regularly** as the project evolves.
- If **new features or modules** are added, ensure to **document them here** for clarity and team collaboration.
- All project group members must **follow this Git repository** and **strictly refer to this README file** for consistent development practices.

---

## ✅ Team Commitment

> 📌 **All members of the project group must follow the guidelines and structure mentioned in this repository. Keeping the documentation up-to-date is a shared responsibility.**

---
