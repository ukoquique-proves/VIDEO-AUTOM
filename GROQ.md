# GROQ.md

This project references the folder:

`/home/uko/CascadeProjects/GROQ/Telegram&GroqAI-`

That folder contains an essay and implementation ideas for using Groq AI in a practical integration scenario (Telegram bot + Groq AI).

**Why is this relevant?**
- The essay provides architectural and design insights for integrating Groq AI with messaging or automation platforms.
- You can mirror or adapt patterns, API usage, or error handling strategies from that example for this project's Groq AI integration layer.

**Action:**
- Review the essay and code in the referenced folder as inspiration or guidance when implementing or extending Groq AI features in this project.

## Implementation Plan for AIService

Based on the reference implementation, the following steps will be taken to integrate Groq AI:

1.  **Create DTOs**:
    -   `ExtractionRequest`: To receive the raw text for processing.
    -   `AIResponse`: To map the structured JSON data returned by Groq.

2.  **Create `AIService`**:
    -   Use `RestTemplate` or `WebClient` for HTTP requests to the Groq API.
    -   Load the Groq API key and model from `application.properties`.
    -   Implement a method to construct the request payload, including a system prompt tailored for data extraction.
    -   Handle API responses and potential errors.

3.  **Create `AIController`**:
    -   Expose a new endpoint, `/api/ai/extract`, to receive `ExtractionRequest` payloads.
    -   Call the `AIService` to process the request and return the structured data.

4.  **Update Configuration**:
    -   Add `groq.api.key`, `groq.model`, and `groq.api.url` to `application.properties`.
