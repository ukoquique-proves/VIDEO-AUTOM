# Project Roadmap

## Goal
Connect logistics data streams to modern communication tools (Slack, Email) by extracting actionable insights from unstructured text.

## Technology Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3
- **Database**: H2 (In-memory)
- **Build Tool**: Maven
- **Integrations**: Groq AI, Slack Webhooks, SMTP Email

---

## Progress Tracking

### Phase 1: Core Implementation ✅
- [x] Analyze output formats and DTO requirements.
- [x] Implement `ExtractionFetchService`.
- [x] Implement `EmailSenderService` and `SlackSenderService`.
- [x] Expose REST endpoints for notification dispatch.
- [x] Integrate AI layer (`AIService`) for real-time extraction.
- [x] Add Global Exception Handling and logging.
- [x] Implement Dockerization.

### Phase 2: Security & Configuration ✅
- [x] Implement Environment Variable strategy for secrets.
- [x] Migrate to `application.yml`.
- [x] Ensure robust `.gitignore` coverage.

### Phase 3: Logistics Showcase Scenarios ✅
- [x] **Scenario 1**: Supplier Invoice Routing.
- [x] **Scenario 2**: Status Updates & Delay Alerts.
- [x] **Scenario 3**: Operations Insights & Summarization.
- [x] Create interactive `demo-guide.md` showcase.

### Phase 4: Strategic Polish ✅
- [x] Professionalize documentation and architectural guides.
- [x] Standardize internal logs and review notes.

### Phase 5: Demo Preparation (Video Readiness) ✅
- [x] **Happy Path Polish**: Refined `/api/send/ai/extract` for edge-case handling.
- [x] **Observability**: Implemented a glassmorphic dashboard (`/index.html`) for real-time logging feedback.
- [x] **Demo Data**: Created a population utility (`/api/demo/populate`) for rapid scenario resets.

### Phase 6: Promotional Video Production
- [ ] **Asset Preparation**
    - [ ] Scenario Setup: The "Logistics Bottleneck" (messy text assets).
    - [ ] UI Setup: Dashboard vs. Swagger UI vs. Slack.
- [ ] **Recording Scenes**
    - [ ] **0:00-0:15**: The Problem (Unstructured logistics chaos).
    - [ ] **0:15-0:40**: The Solution (AI-driven extraction).
    - [ ] **0:40-0:50**: The "Wow" Factor (Instant Slack/Email alerts).
    - [ ] **0:50-1:00**: The Closing (Developer profile and contact).
- [ ] **Marketing**
    - [ ] Design high-impact thumbnail.
    - [ ] Global rollout (LinkedIn/Discord).

### Phase 7: Architectural Evolution (Future)
- [ ] **Hexagonal Architecture**: Define `AIProvider` ports to decouple core logic from specific API adapters.
- [ ] **Multi-Provider Support**: Add OpenAI and Gemini adapters.
- [ ] **Advanced Integrations**: Notion, Discord, and scheduled digests.
