# AI Integration Guide

This project integrates with LLM providers to perform intelligent data extraction. The primary integration currently uses **Groq AI**.

## Architectural Design

The integration follows a structured approach to ensure reliability and ease of extension:

1.  **DTO-based Communication**:
    -   `ExtractionRequest`: Standardizes the raw text input.
    -   `AIResponse`: Maps the structured JSON returned by the AI provider.

2.  **Service Layer (`AIService`)**:
    -   Uses `RestTemplate` for high-performance HTTP requests.
    -   Configuration is externalized via `application.yml`, loading `groq.api.key`, `groq.model`, and `groq.api.url`.
    -   Implements specialized system prompts to ensure the AI responds in pure, valid JSON.
    -   Includes robust parsing logic to handle Markdown code blocks and whitespace in AI responses.

## Future Provider Support

While the current implementation focuses on Groq, the architecture is designed to support:
- **OpenAI**: Via the OpenAI Chat Completions API.
- **Gemini**: Via Google's Generative AI API.

Integration patterns (API usage, error handling, and prompt engineering) can be adapted from the existing Groq implementation to these additional providers.
