# Changelog

All notable changes to this project will be documented in this file.
 
## [1.2.1] - 2026-04-26

### Added
- **Automated Video Showcase**: Implemented and executed a Playwright-based recording script (`record.js`) to generate a professional .webm showcase, covering the dashboard, Swagger UI extraction, and real-time population scenarios.
- **Roadmap Update**: Marked the Video Production phase as completed in `roadmap.md`.

### Changed
- **Architectural Refactoring**: Renamed `ExtractionFetchService` to `ExtractionService` across the entire codebase (including controllers and tests) to better reflect its comprehensive role in handling both data persistence and retrieval.
- **Service-Layer Documentation**: Added detailed Javadoc to `ExtractionService` and `AIService` to formally define their architectural boundaries (Persistence vs. Orchestration logic).

### Fixed
- **Naming Ambiguity**: Resolved the misleading "fetch-only" connotation of the service layer by adopting a more neutral and accurate naming convention.

## [1.1.2] - 2026-04-25

### Added
- **Video Production Roadmap**: Restored the "Promotional Video Production" phase (Phase 6) to `roadmap.md`, including the 0:00-1:00 recording script and marketing strategy.
- **Showcase Milestones**: Re-included "Demo Preparation" (Phase 5) in the roadmap to track dashboard observability and demo-data population utilities.

### Added
- **AI Extraction Prompt Optimization**: Refined the system prompt in `AIService` to enforce flat JSON output, specifically resolving the 4/5 record creation failure during demo population of complex operation logs.

### Fixed
- **SMTP Authentication Compliance**: Secured the email integration by migrating to a Google App Password, ensuring reliable SMTP authentication and better account protection.

## [1.1.0] - 2026-04-25

### Added
- **Professional Documentation Suite**: Consolidated all project metadata into a clean `docs/` directory, including a new `roadmap.md`, `ai-integration.md`, `demo-guide.md`, `troubleshooting.md`, and `review-notes.md`.
- **Internal Development Logs**: Moved session-specific logs and developer notes to `.github/INTERNAL_LOGS.md` to maintain a professional, visitor-facing `docs/` folder.
- **Logistics Showcase Scenarios**: Implemented advanced extraction scenarios for Supplier Invoices, Shipment Delays, and Daily Ops Logs.
- **Enhanced AI Model**: Expanded `AIResponse` DTO with `status`, `category`, and `isUrgent` fields to support specialized logistics logic.
- **Advanced Prompting**: Upgraded `AIService` system prompts to perform intelligent categorization and urgency detection.
- **Rich Assets**: Added `invoice-routing.txt`, `status-delay.txt`, and `logistics-summary.txt` to `demo-assets/` for rapid stakeholder demonstrations.
- **Storytelling Documentation**: Created `demo-guide.md` (previously `DEMO.md`) which translates technical features into business-value stories with runnable examples.
- **Urgency-Aware Notifications**: Updated `MessageFormatter` and notification services to visually flag urgent extraction results with clear warnings.
- **Comprehensive Testing**: Added `testAdvancedShowcaseExtraction` to `AIServiceTest` and verified entire 31-test suite success.
- **Architectural Cleanup**: Standardized constructor injection by removing redundant `@Autowired` annotations from all Controllers and Services, aligning with modern Spring Boot standards.
- **Robust Data Mapping**: Restored `@JsonProperty("totalAmount")` in `AIResponse` DTO to ensure reliable deserialization from LLM responses regardless of casing.
- **Observability Dashboard**: Developed a real-time, glassmorphic monitoring dashboard (`/index.html`) featuring semantic status badges and live extraction statistics.
- **Enhanced Data Persistence**: Updated the `Extraction` entity, DTOs, and Mapper to fully persist and expose new logistics fields (`status`, `category`, `isUrgent`).
- **Semantic Status UI**: Implemented an intelligent status-to-color mapping system in the dashboard (Delivered → Green, Pending → Yellow, Delayed → Orange) for instant visual feedback.
- **New Retrieval API**: Introduced `ExtractionController` to provide a dedicated endpoint for fetching stored extraction records.
- **Notification Consistency**: Unified all notification headers to "Logistics Data Extraction" across AI and database-driven workflows for a professional, cohesive user experience.
- **Demo Data Loader Endpoint**: Added `/api/demo/populate` to process all `.txt` files from `demo-assets/` through the AI extraction pipeline and persist results for live demos.
- **Interactive Dashboard Controls**: Added "Run Demo Scenarios" and "Reset Database" actions to `static/index.html` for one-click showcase flows.
- **Critical Demo Asset**: Added `demo-assets/urgent-critical.txt` to exercise urgency/status behavior with a high-priority incident scenario.
- **JSON Contract Probe Endpoint**: Added `GET /api/extractions/sample` to verify frontend JSON shape compatibility without requiring live AI calls.

### Changed
- **Swagger UI Standardization**: Standardized all Swagger UI references to the consistent `/swagger-ui/index.html` path and updated `application.yml` to ensure technical alignment.
- **README Transparency**: Refined the README to clearly distinguish between the active Groq integration and planned OpenAI/Gemini support, including updated architecture diagrams.
- **Project Metadata Accuracy**: Corrected malformed tags in `pom.xml` and updated the project URL to the official repository.
- **Roadmap Evolution**: Updated roadmap documentation to include Phase 4 (Showcase Scenarios) as a completed strategic requirement.
- **Unified Notification Formatting**: Refactored `MessageFormatter` so both the AI path and the DB retrieval path use a single `format()` method, ensuring `category`, `status`, and `isUrgent` are always included in email/Slack messages regardless of how data was sourced.
- **Exception Handler Ordering**: Reordered handlers in `GlobalExceptionHandler` from most-specific to most-generic (`MethodArgumentNotValidException` → `ResourceNotFoundException` → `NotificationException` → `IllegalArgumentException` → `RuntimeException` → `Exception`) to eliminate the misleading dead-path appearance and align with Spring convention.
- **Testable Architecture Refactoring**: Refactored `SlackSenderService` to use constructor-based dependency injection for the `Slack` client, decoupling it from static factory methods and enabling total isolation through Mockito.
- **Improved Test Stability**: Fixed a locale-dependent formatting bug in `MessageFormatter` by enforcing `Locale.US`, ensuring consistent number formatting (decimal separators) regardless of the host environment.
- **Performance Optimization**: Refactored `SlackSenderService` to initialize the Slack client once in the constructor, preventing resource leaks and aligning its lifecycle with the Spring bean.
- **Architectural Refactoring**: Decoupled `SendController` from persistence logic by moving repository and mapper dependencies into `ExtractionFetchService`, strictly adhering to layered architecture principles.
- **Professional Documentation Upgrade**: Re-structured `README.md` for better positioning and accurate showcases.
- **Architectural Evolution Plan**: Updated roadmap documenting the transition toward a provider-agnostic Hexagonal Architecture.
- **AI-Agnostic Vision**: Formally included OpenAI and Gemini as future supported providers in project goals.
- **Production Container Hardening**: Updated Docker base image from `eclipse-temurin:17-jdk-alpine` to `eclipse-temurin:17-jre-alpine` to reduce runtime image size and attack surface.
- **Docker Runtime Compatibility**: Updated `Dockerfile` to copy `demo-assets/` into the image and corrected the artifact copy path to `target/autom-hub-0.0.1-SNAPSHOT.jar`.
- **Project Naming Consistency**: Replaced legacy "API Bridge" branding with "AI Logistics Automation Hub" across documentation, OpenAPI metadata, utility output text, and verification scripts.
- **Contribution Guidelines Upgrade**: Rewrote `CONTRIBUTING.md` with professional standards for architecture, branching, commit style, testing flow, and contribution quality.
- **Service-Layer Boundary Enforcement**: Refactored `DemoController` to remove direct repository access and route reset operations through `ExtractionFetchService` via `clearAll()`.
- **ObjectMapper Centralization**: Moved `ObjectMapper` instantiation out of `AIService` and into `RestConfig` as a Spring bean for centralized JSON configuration.

### Fixed
- **Defensive Logic**: Corrected a potential `NullPointerException` in `AIService` by ensuring input validation occurs before any processing or logging.
- **Demo Populate False Success**: Updated `/api/demo/populate` to return accurate outcomes (created/failed counts), return `500` when zero records are created, and include failure context instead of always returning success.
- **Dashboard Feedback Accuracy**: Updated the demo run button flow to display the backend response message and refresh extraction data immediately after populate attempts.
- **Demo Asset Data Quality**: Corrected `demo-assets/sample-invoice.txt` amount from `,450.50` to `$1,450.50` to remove extraction ambiguity in live demos.
- **Serialization Stability**: Locked `ExtractionResponse` output field names with explicit Jackson annotations (`companyName`, `date`, `totalAmount`, `category`, `status`, `isUrgent`) to prevent silent frontend breakage from future naming-strategy changes.
- **Credential Exposure Cleanup**: Removed embedded GitHub token from local git remote configuration and reset `origin` to a token-free repository URL.

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
