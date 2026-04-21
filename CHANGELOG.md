# Changelog

All notable changes to this project will be documented in this file.

## [0.6.0] - 2026-02-02

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
