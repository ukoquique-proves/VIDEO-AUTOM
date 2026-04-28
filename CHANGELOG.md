# Changelog

All notable changes to this project will be documented in this file.

## [1.2.5] - 2026-04-28

### Added
- **Dynamic Timing Synchronization**: Implemented a `metadata.json` bridge between `record.js` and `merge_video_audio.js`. The engine now tracks *actual* scene start times during recording, ensuring frame-perfect audio alignment even if browser actions experience latency.
- **Audio Length Safety Validation**: Integrated `ffprobe` checks into `generate_audio.js` to proactively warn if narration text exceeds the allocated scene duration, preventing audio overlaps before they happen.
- **Extended 2-Minute Showcase**: Added `scenario-2min.js` for an in-depth 120-second demonstration of the AI Logistics Hub, including H2 console and Swagger UI exploration.

### Changed
- **Automated Workspace Hygiene**: The `merge_video_audio.js` script now automatically deletes temporary audio blocks and metadata files upon successful video generation.
- **Improved Recording Resilience**: Updated `record.js` to save timing metadata even in the event of a partial script failure, allowing for synchronized partial video exports.
- **Enhanced Scenarios**: Refined `scenario-2min.js` with increased scene durations and more robust `waitForSelector` patterns to ensure 5/5 record population reliability.
- **Git Hygiene**: Updated `.gitignore` to support the new scenario-specific video directory structure and ignore all `metadata.json` artifacts.

### Fixed
- **Audio/Video Desync**: Resolved the critical drift issue where narration would desync from visuals in videos longer than 60 seconds.
- **Debug Artifact Leak**: Removed the Scene ID overlay from production recordings to ensure a clean, professional output.
- **Playwright Environment**: Re-installed and verified Chromium browsers for the current execution environment.

### Removed
- **Legacy Video Artifacts**: Deleted redundant error screenshots and Playwright-generated random-UUID `.webm` files from the repository.

## [1.2.4] - 2026-04-28

> [!IMPORTANT]
> **Video Production Status**: The timing and synchronization issues noted in the previous release have been fully resolved in v1.2.5.

### Added
- **General-Purpose Video Engine**: Refactored the video production pipeline into a reusable engine located in `video-recorder/engine/`.
- **Scenario-Driven Architecture**: The engine is now completely decoupled from project-specific logic. All URLs, selectors, and narration live in project-specific scenario files under `video-recorder/scenarios/`.
- **Scenario Template**: Added `video-recorder/scenarios/_template/scenario.js` as a starter for new video projects.
- **Scenario Synchronization**: These improvements were applied to both standard and 2-minute scenarios.
- **Defensive UI Interactions**: Integrated `waitFor({ state: 'visible' })` and stabilization pauses for Swagger UI interactions, inspired by robust procedures in the reference project.
- **Logistics Hub Scenario**: Migrated the existing AI Logistics Hub demo into its own scenario folder (`video-recorder/scenarios/logistics-hub/`).

### Changed
- **Pipeline Execution**: Updated npm scripts in `video-recorder/package.json` to require a `--scenario` argument (e.g., `npm run build-video --scenario=scenarios/logistics-hub/scenario.js`).
- **Path Standardization**: Consolidated all output artifacts (video, audio, screenshots) into the scenario-specific `videos/` folder.
- **Documentation Consolidation**: Merged `VIDEO_STEPS.md` into `RECORDING.md` and deleted the redundant file.
- **Roadmap Synchronization**: Updated `roadmap.md` to reflect the persistent H2 database state and the removal of the `atempo` audio filter.

### Fixed
- **Stale Documentation**: Corrected multiple references to "in-memory H2" in `roadmap.md` and `verify_usage.sh` following the migration to file-based persistence.
- **Screenshot Path**: Fixed `record.js` to write the error screenshot into the `videos/` directory instead of the project root.

## [1.2.3] - 2026-04-27


### Added
- **Video Pipeline Pre-flight Checks**: `record.js` now validates both server availability and `demo` Spring profile activation before launching Playwright, aborting with an actionable error message if either check fails.
- **Audio Duration Validation**: `merge_video_audio.js` uses `ffprobe` to measure each audio block's real duration and aborts the FFmpeg merge with a clear error if any block would overlap the next scene's start timestamp.
- **Convenience npm Scripts**: Added `generate-audio`, `record`, `merge`, and `build-video` scripts to `video-recorder/package.json` for single-command pipeline execution.

### Changed
- **Demo Profile Gating**: `DemoController` is now annotated with `@Profile("demo")`, ensuring `/api/demo/reset` and `/api/demo/populate` endpoints are completely absent from production deployments. Start with `mvn spring-boot:run -Dspring-boot.run.profiles=demo` to enable them.
- **Video-Recorder Dependency Isolation**: Moved `gtts` dependency from the root `package.json` into `video-recorder/package.json`. The root `package.json` and `package-lock.json` have been removed so video tooling no longer leaks into the Java project root.
- **H2 Persistence**: Switched datasource URL from `jdbc:h2:mem:testdb` (in-memory) to `jdbc:h2:file:./h2_data/testdb` (file-based) to ensure extraction records survive server restarts and multi-session demo recordings.
- **Duplicate Audio Script Removed**: Deleted `video-recorder/generate_audio.py` (Python/gTTS) to establish `generate_audio.js` as the single source of truth for narration text and TTS generation.
- **Orphan Artefact Removed**: Deleted `video-recorder/videos/list.txt`, a leftover from a previous `ffmpeg -f concat` approach that was no longer referenced anywhere.
- **Recording Timing Aligned to Audio**: Extended `waitForTimeout` durations in `record.js` to produce a ~60-second video, matching the timestamp offsets expected by `merge_video_audio.js` audio blocks.
- **Video File Naming Fixed**: `record.js` now captures `page.video().path()` after context close and renames the Playwright-generated random-UUID `.webm` to the canonical `final_showcase.webm` expected by `merge_video_audio.js`.
- **RECORDING.md Accuracy**: Corrected documentation that incorrectly claimed `atempo` was used; updated to reflect the current approach of naturally-paced, pre-shortened audio blocks.

### Fixed
- **`AIResponse` Unknown Field Crash**: Added `@JsonIgnoreProperties(ignoreUnknown = true)` to `AIResponse`, preventing `UnrecognizedFieldException` when the LLM includes extra fields (e.g., `additionalInfo`) beyond the defined DTO schema. Demo now reliably creates 5/5 records.
- **Nested Output Directory Bug**: Removed `generate_audio.py` which used a relative path `video-recorder/videos/audio_blocks` and would silently create a nested `video-recorder/video-recorder/videos/` directory if run from inside the `video-recorder/` folder.
- **Final Audio Block Overlap**: Shortened the `block_55` narration text so its TTS output fits within the 5-second slot available before end-of-video.

### Security / Hygiene
- **`.gitignore` Hardened**: Added `node_modules/`, `h2_data/`, and `video-recorder/videos/` to prevent build artefacts, database files, and generated media from being accidentally committed.

## [1.2.2] - 2026-04-26

### Added
- **Intelligent Audio Overlap Prevention**: Enhanced the `merge_video_audio.js` script with FFmpeg `atempo` filters to automatically compress narration blocks that exceed their visual scene duration.
- **Automated Narration Pipeline**: Fully automated the generation of voiceovers and their synchronization with the video showcase.

### Changed
- **Production Documentation**: Updated `RECORDING.md` and `NARRATION_SCRIPT.md` to document the automated audio pacing logic.

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
