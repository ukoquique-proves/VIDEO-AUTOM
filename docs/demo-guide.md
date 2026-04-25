# Logistics Automation Showcase — Demo Scenarios

This document outlines concrete business scenarios demonstrating the **AI Logistics Automation Hub**.

---

## Scenario 1: The Port Bottleneck (Urgent Delay Alert)

**Input Asset:** `demo-assets/status-delay.txt`
**AI Logic:** Identifies `DELAYED` status and flags `isUrgent: true`.
**Action:** Triggers a high-priority Slack notification.

**Trigger Command (run from project root):**
```bash
curl -X POST http://localhost:8080/api/send/ai/slack \
     -H "Content-Type: application/json" \
     -d "{\"text\": \"$(cat demo-assets/status-delay.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
```

---

## Scenario 2: Finance Automation (Supplier Invoice Routing)

**Input Asset:** `demo-assets/invoice-routing.txt`
**AI Logic:** Categorizes as `Invoice`, extracts `companyName`, `date`, and `totalAmount`.
**Action:** Routes structured JSON to the Finance department's email.

**Trigger Command (run from project root):**
```bash
curl -X POST "http://localhost:8080/api/send/ai/email?to=finance@example.com" \
     -H "Content-Type: application/json" \
     -d "{\"text\": \"$(cat demo-assets/invoice-routing.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
```

---

## Scenario 3: Operations Digest (Daily Log Insights)

**Input Asset:** `demo-assets/logistics-summary.txt`
**AI Logic:** Processes mixed updates, identifying completed tasks vs. blocked routes.
**Action:** Generates a structured JSON summary highlighting `PENDING` statuses.

**Trigger Command (run from project root):**
```bash
curl -X POST http://localhost:8080/api/send/ai/extract \
     -H "Content-Type: application/json" \
     -d "{\"text\": \"$(cat demo-assets/logistics-summary.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
```

---

## How to Run the Demo

1.  **Start Application**: `mvn spring-boot:run`
2.  **Configure Env**: Ensure `.env` has valid `GROQ_API_KEY`, `EMAIL_USERNAME`, and `SLACK_WEBHOOK_URL`.
3.  **Execute Commands**: Run the cURL commands above.
4.  **Explore API**: Visit [Swagger UI](http://localhost:8080/swagger-ui/index.html).
