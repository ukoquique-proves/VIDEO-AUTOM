# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- **Unit Test Suite**: Introduced a comprehensive suite of 12 pure JUnit 5 tests covering core logic in `MessageFormatter`, `ExtractionMapper`, `SlackSenderService`, and `ExtractionFetchService`.
- **Testable Architecture Beans**: Added a `Slack` bean to `RestConfig` to enable dependency injection for the Slack client to support constructor-based injection.
- **Bean Validation**: Integrated `spring-boot-starter-validation` and implemented `@NotBlank` constraints on `ExtractionRequest` to ensure data integrity at the API boundary.
- **Improved Test Isolation**: Added `application-test.yml` and utilized `@ActiveProfiles("test")` to decouple the test suite from production environment variables and external API keys.
- **Robust Error Handling**: Updated `GlobalExceptionHandler` to provide structured JSON responses for validation failures, improving API consumer experience.

### Changed
- **Testable Architecture Refactoring**: Refactored `SlackSenderService` to use constructor-based dependency injection for the `Slack` client, decoupling it from static factory methods and enabling total isolation through Mockito.
- **Improved Test Stability**: Fixed a locale-dependent formatting bug in `MessageFormatter` by enforcing `Locale.US`, ensuring consistent number formatting (decimal separators) regardless of the host environment.
- **Performance Optimization**: Refactored `SlackSenderService` to initialize the Slack client once in the constructor, preventing resource leaks and aligning its lifecycle with the Spring bean.
- **Architectural Refactoring**: Decoupled `SendController` from persistence logic by moving repository and mapper dependencies into `ExtractionFetchService`, strictly adhering to layered architecture principles.
- **Professional Documentation Upgrade**: Re-structured `README.md` for better positioning and accurate showcases.
- **Architectural Evolution Plan**: Added Phase 6 to `PLAN.md` documenting the transition toward a provider-agnostic Hexagonal Architecture.
- **AI-Agnostic Vision**: Formally included OpenAI and Gemini as future supported providers in project goals.

### Fixed
- **Defensive Logic**: Corrected a potential `NullPointerException` in `AIService` by ensuring input validation occurs before any processing or logging.

## [1.0.0] - 2026-04-21

### Added
- **Demo Utility**: Created `DemoController` with a `/api/demo/reset` endpoint to facilitate repeatable demonstrations.
- **Global Error Handling**: Implemented `GlobalExceptionHandler` to ensure professional and consistent API error responses.
- **Observability**: Added SLF4J logging to `AIService` for real-time monitoring during demo recordings.
- **Demo Assets**: Added `demo-assets/sample-invoice.txt` for immediate use in demonstrations.

### Changed
- **Improved AI Robustness**: Enhanced `AIService` with strict input validation and refined prompting logic for more reliable data extraction.
- **Unified Documentation**: Consolidated `PLAN-UPLOAD.md` and `PLAN-DEMO.md` into the main `PLAN.md` for better project clarity.
- **Notification UX**: Refined `MessageFormatter` templates with professional layout and emojis for better visual impact in Slack and Email.

## [0.5.0] - 2026-02-01

### Added
- **Production Security**: Migrated configuration to `application.yml` with environment variable support for all sensitive credentials (Groq API, Email, Slack).
- **Repository Files**: Added MIT `LICENSE` and `CONTRIBUTING.md` for professional open-source standards.
- **Git Protection**: Created comprehensive `.gitignore` to prevent accidental exposure of secrets and build artifacts.

### Changed
- **Configuration Migration**: Replaced `application.properties` with `application.yml` using `${ENV_VAR}` placeholders for all credentials.
- **Documentation**: Updated `README.md` to remove redundant API endpoint listings (now covered by Swagger UI).

### Removed
- **Insecure Configuration**: Deleted `application.properties` containing hardcoded credentials.

## [0.4.0] - 2026-02-01

### Added
- **Swagger/OpenAPI Documentation**: Integrated `springdoc-openapi` to provide automated, interactive API documentation and testing UI at `/swagger-ui.html`.
- **API Annotations**: Annotated `SendController` and DTOs (`ExtractionRequest`, `AIResponse`, `ExtractionResponse`) with detailed Swagger descriptions for better developer experience.
- **Final Verification**: Verified build success and passed all integration tests.

## [0.3.1] - 2026-01-31

### Fixed
- **AI Response Parsing**: Fixed a crash where Groq AI returned Markdown code blocks (` ```json ... ``` `) by implementing logic to strip them before parsing.
- **Dependency Conflicts**: Removed `org.json` dependency to resolve classpath conflicts with Spring Boot's Android JSON library.

### Changed
- **JSON Handling**: Refactored `AIService` to use Jackson (`ObjectMapper`) instead of `org.json` for consistent and robust JSON processing.
- **Code Optimization**: Unified duplicate string formatting logic in `MessageFormatter` to improve maintainability.
- **Constructor Injection**: Refactored ALL Controllers and Services to use Constructor Injection instead of Field Injection for better testability, immutability, and explicit dependencies.
- **API Consolidation**: Moved `/api/ai/extract` endpoint to `/api/send/ai/extract` and removed redundant `AIController` for a unified API structure.

## [0.3.0] - 2026-01-31

### Added
- **MessageFormatter Utility**: Created a centralized utility class for formatting extraction and AI responses.
- **Project Documentation**: Enhanced README.md with architectural principles and classic project information.

### Changed
- **Code Refactoring**: Moved duplicate formatting logic from `EmailSenderService` and `SlackSenderService` to `MessageFormatter`.
- **Test Improvements**: Updated test mocks to use the new `MessageFormatter` utility.

### Removed
- **Unused Dependencies**: Removed Lombok dependency as it wasn't being utilized in the project.
- **Obsolete Mock Services**: Deleted `MockEmailSenderService` and `MockSlackSenderService` which were causing build issues.

## [0.2.0] - 2026-01-31

### Added
- **Docker Support**: Added `Dockerfile` for containerization. Successfully built and ran the application in a container as part of final validation.
- **Groq AI Integration**: Implemented `AIService` to connect to the Groq API for intelligent data extraction from raw text.
- **AI Controller**: Added `AIController` with a new endpoint (`/api/ai/extract`) to expose AI functionality.
- **New DTOs**: Created `ExtractionRequest` and `AIResponse` for handling AI data flow.
- **End-to-End AI Testing**: Added an integration test (`AIServiceIntegrationTest`) to verify live calls to the Groq API.
- **Project Planning**: Added `PLAN.md` and `GROK.md` to document project goals and AI implementation strategy.
- **Changelog**: Created this `CHANGELOG.md` to track project progress.
- **AI-Powered Sending**: Integrated `AIService` with `SendController` to create new endpoints (`/api/send/ai/email` and `/api/send/ai/slack`) for sending AI-extracted data.

### Changed
- **Improved Data Model**: Refactored the `Extraction` model and `ExtractionResponse` DTO to use `Double` for `totalAmount` instead of `String` for better data integrity.
- **Enhanced Sender Services**: Updated `EmailSenderService` and `SlackSenderService` to handle `AIResponse` DTOs.

### Fixed
- **Final Validation**: Successfully completed all planned steps, including containerization and end-to-end testing.
- **JSON Payload Format**: Corrected the request payload sent to the Groq API by using a `JSONArray` for the `messages` field, resolving a `400 Bad Request` error.
- **JSON Deserialization**: Fixed a recurring `UnrecognizedPropertyException` during JSON parsing by adding an explicit `jackson-databind` dependency to the `pom.xml`, resolving classpath conflicts.
- **Missing Dependency**: Added the `org.json` dependency to `pom.xml` to fix compilation errors in `AIService`.
- **Test Dependencies**: Correctly configured mock beans in `SendControllerAIIntegrationTest` to resolve dependency injection issues.

## [0.1.0] - 2026-01-26

### Added
- **Initial Project Setup**: Created the Spring Boot application with JPA, H2, and Web starters.
- **Core Data Model**: Implemented the `Extraction` entity and repository.
- **Email Integration**: Added `EmailSenderService` to send extraction data to a specified email address.
- **Slack Integration**: Added `SlackSenderService` to send extraction data to a configured Slack webhook.
- **REST API**: Created `SendController` to expose endpoints for triggering email and Slack notifications.
- **Integration Tests**: Added initial integration tests for the `SendController` using mock services.
