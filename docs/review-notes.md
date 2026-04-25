# Review Notes & Best Practices

This document outlines key quality control points and architectural refinements for the AI Logistics Automation Hub.

## 1. Quality Control: Build Artifacts

Ensure that the `.git` and `target` folders are excluded from the final distribution.

- **Issue**: Including `target/` contains compiled `.class` files which should not be in source control.
- **Fix**: Verify that `.gitignore` correctly includes `/target/`. Only source code (`src/`), configuration (`pom.xml`, `.gitignore`), and documentation should be tracked.

## 2. Architecture & Design Refinements

To align with professional Solution Architect standards, consider the following improvements:

### Custom Exception Handling
The `ExtractionFetchService` and other core services should avoid returning `null` or raw `500` errors.
- **Recommendation**: Use a `@ControllerAdvice` class (already implemented in `GlobalExceptionHandler.java`) to handle custom exceptions like `NotificationException` or `ResourceNotFoundException` and return consistent JSON error messages.

### Environment-Based Security
Sensitive information such as API keys must never be hardcoded.
- **Verification**: Ensure `AIService.java` pulls the Groq API key from environment variables (e.g., `${GROQ_API_KEY}`) via Spring's `@Value` annotation.

## 3. Repository Standards

- **Prerequisites**: Clearly state requirements (Java 17+, Maven, API keys) in the main documentation.
- **Deployment**: Maintain a `Dockerfile` for containerized environments to ensure a "Run in 2 minutes" experience for reviewers.
