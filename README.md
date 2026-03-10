# Project Architecture Overview

## Introduction

This project is an Android application built using modern Android
development best practices, aimed to give information about many famous cocktails.\
It follows a clean and modular architecture approach to ensure
scalability, maintainability, and testability.\
This project also uses core utility methods defined in B.U.F.F.A. Android
library (github repo link: [BUFFA repo](https://github.com/ZioAnacleto/BaseUtilityFunctionsForAndroid))
To retrieve the correct data I wanted, I've also created a custom database and
developed a dedicated server for the app using **Ktor**.\
The source code can be found here:
[SpeakEazy BE](https://github.com/ZioAnacleto/SpeakEazyBackEnd).

------------------------------------------------------------------------

## Architecture

The project follows a **Clean Architecture + MVVM
(Model-View-ViewModel)** pattern. The different layers
are divided in different modules, to better encapsulate
each layer.

### Layers

### 1. Presentation Layer

-   Defined in app module
-   Built with **Jetpack Compose**
-   Uses **State-driven UI**
-   Observes state from ViewModels using Kotlin Flows

### 2. Domain Layer

-   Defined in **core:domain** module
-   Contains business logic
-   Use cases encapsulate application-specific rules
-   Independent from Android framework

### 3. Data Layer

-   Defined in **core:data** module
-   Repository pattern implementation
-   Maps DTOs to domain models via **DataMapper** interface

### 4. Additional Layers

-   Defined modules for **network**, **local database** and **analytics**
-   Local data source (Room database)
-   Remote data source (Ktor HTTP client)

------------------------------------------------------------------------

## State Management

-   Uses `StateFlow` for reactive state handling
-   UI reacts to state updates
-   Sealed classes represent UI states
-   Single source of truth per screen

------------------------------------------------------------------------

## Networking

-   HTTP client built with **Ktor**
-   Custom API client abstraction
-   Error handling based on HTTP status codes
-   Handles edge cases like `204 No Content`

------------------------------------------------------------------------

## Local Persistence

-   **Room Database**
-   DAO pattern
-   Entities for database models
-   Supports upsert operations

------------------------------------------------------------------------

## Dependency Injection

-   **Koin**
-   ViewModels and repositories injected via modules
-   Improves testability and separation of concerns

------------------------------------------------------------------------

## Asynchronous Programming

-   **Kotlin Coroutines**
-   Structured concurrency
-   Lifecycle-aware state collection

------------------------------------------------------------------------

## Testing

-   Unit tests with:
    -   **MockK** for mocking
    -   Coroutine test utilities
    -   Android Test for Room DAOs

------------------------------------------------------------------------

## Performance Monitoring

-   Custom performance tracing abstraction
-   Measures execution time of critical operations
-   Helps identify slow database or network operations

------------------------------------------------------------------------

## UI/UX Features

-   Animated transitions using Compose animations
-   Timed banners with progress indicators
-   Loading overlays
-   Responsive grid layout without nested lazy components
-   Keyboard handling improvements

------------------------------------------------------------------------

## CI/CD

-   Github actions using pull requests
-   Running unit tests and instrumented tests to ensure code quality
-   Code coverage threshold **80%**

------------------------------------------------------------------------

## Technologies Used

### Core

-   Kotlin
-   Jetpack Compose
-   Coroutines
-   Flow

### Networking

-   Ktor
-   Kotlinx Serialization

### Persistence

-   Room Database

### Dependency Injection

-   Koin

### Testing

-   JUnit
-   MockK
-   Coroutine Test APIs
-   JaCoCo code coverage

### Other

-   Material Design 3
-   Custom animation components
-   Proprietary plugins for code generation and JaCoCo code coverage

------------------------------------------------------------------------

## Key Design Principles

-   Separation of concerns
-   Single source of truth
-   Unidirectional data flow
-   Testability-first approach
-   Modular and scalable structure

------------------------------------------------------------------------

## Conclusion

The project is structured to support growth and maintain high
maintainability standards.\
By combining Clean Architecture principles with modern Android tools
such as Jetpack Compose and Ktor, it ensures a robust and scalable
codebase ready for production environments.
If you would like to help me improve this project, maintaining the current codebase
or by giving advices to optimize its structure, or simply if you want to
have a chat on this project, feel free to write me an email:
[Hello there](mailto:traversa.fabrizio+speakeazyapp@gmail.com?subject=General%20Kenoby&body=Hi%20Fabrizio!).