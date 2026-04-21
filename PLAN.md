# PLAN.md

## Project: The API Bridge

### Goal
Connect a legacy system (CSV/SQL) to modern tools (Slack, Notion, Discord) by sending extracted results from Project_1 directly to a client's email or Slack channel.

### Value
Demonstrate integration of old software outputs with new communication platforms, bridging business data to actionable channels.

### Technology Stack (inherited from Project_1)
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: H2 (in-memory, can extend to SQL)
- **Build Tool**: Maven/Gradle (as per Project_1)
- **Testing**: JUnit

### Coding Style
- Use the same package, class, and method conventions as Project_1.
- DTOs, Service, Controller layers.
- Dependency injection via Spring annotations.
- Exception handling and logging as in Project_1.

### High-Level Architecture
1. **Input**: Extracted results from Project_1 (via API or DB access).
2. **Processing**: Format results for delivery.
3. **Output**: Send results to:
    - Email (via SMTP, e.g., JavaMailSender)
    - Slack (via Slack API)

---

## Roadmap

### Phase 1: Core Implementation
- [x] Analyze Project_1 output format (DTO: ExtractionResponse).
- [x] Implement a service to fetch recent extractions.
- [x] Implement an email sender service.
- [x] Implement a Slack sender service.
- [x] Expose endpoints to trigger sending (POST: send-to-email, send-to-slack).
- [x] Configuration for email/Slack credentials.
- [x] Mock email and Slack integrations for local/testing.
- [x] Use numeric types (Double) for totals (totalAmount) in model and DTO.
- [x] Rerun tests with numeric types.
- [x] Add AI integration layer/service (real or simulated LLM call).
- [x] Improve error handling (custom exceptions, global handler).
- [x] Add API documentation (Swagger/OpenAPI).
- [x] Add Dockerfile for containerization.
- [x] Final testing and validation.

### Phase 2: Security & Production Readiness
- [x] **Implement Environment Variable Strategy**
    - [x] Modify `AIService.java` to use `@Value("${ai.api.key}")`.
    - [x] Verify `EmailSenderService` and `SlackSenderService` credentials.
- [x] **Migrate Configuration**
    - [x] Create `application.yml` and migrate from `.properties`.
    - [x] Delete `application.properties`.
- [x] **Secure Git History**
    - [x] Robust `.gitignore` for secrets.
    - [x] Clean Git logs of any leaked keys.

### Phase 3: Strategic Polish & GitHub Upload
- [x] **Repository "Curb Appeal"**
    - [x] Add MIT `LICENSE`.
    - [x] Add `CONTRIBUTING.md`.
- [x] **Final Upload**
    - [x] Initialize Git and Push to GitHub.
    - [x] Verify live repository status.

### Phase 4: Demo Preparation (Video Readiness)
- [x] **Polish the "Happy Path"** ✅ **Must-Have**
    - [x] Robust `/api/send/ai/extract` (handle edge cases).
    - [x] Sample input assets (messy email/invoice text).
- [x] **Demo Data & UX** ✅ **Must-Have**
    - [x] Database reset script for repeated runs.
    - [x] Professional Message templates (Email/Slack).
- [x] **Observability** 🎯 **Nice-to-Have**
    - [x] Real-time logging feedback for video.
    - [x] Simple status dashboard.

### Phase 5: Promotional Video Production
*Referenced from Promo-VIDEO script*
- [ ] **Asset Preparation**
    - [ ] Scenario Setup: The Logistics Bottleneck (Messy text).
    - [ ] UI Setup: Swagger UI vs Postman.
- [ ] **Recording Scenes**
    - [ ] 0:00-0:15: The Problem.
    - [ ] 0:15-0:40: The Solution (AI Extraction).
    - [ ] 0:40-0:50: The "Wow" Factor (Notifications).
    - [ ] 0:50-1:00: The Closing (Profile).
- [ ] **Marketing**
    - [ ] Design high-impact Thumbnail.
    - [ ] Global rollout (LinkedIn/Discord).

---

## Stretch Goals
- Add Notion/Discord integration.
- Schedule automatic sending (Spring Scheduler).
- UI for manual triggering (optional).