# Logistics Automation Showcase — Demo Scenarios

This document outlines the concrete business scenarios used to demonstrate the power of **The API Bridge**. These scenarios go beyond simple text-to-JSON extraction, showing how AI provides actionable insights for logistics teams.

---

## Scenario 1: The Port Bottleneck (Urgent Delay Alert)

**The Story:** A shipment containing temperature-sensitive electronics is stuck at the Port of Montevideo due to labor issues. Every hour of delay increases the risk of cargo damage.

*   **Input Asset:** `demo-assets/status-delay.txt`
*   **AI Logic:** The system identifies the `DELAYED` status and flags the extraction as `isUrgent: true`.
*   **Action:** A high-priority Slack notification is triggered with a clear `⚠️ URGENT ACTION REQUIRED` warning, alerting the operations team to reroute or contact the terminal immediately.
*   **Trigger Command:**
    ```bash
    curl -X POST http://localhost:8080/api/send/ai/slack \
         -H "Content-Type: application/json" \
         -d "{\"text\": \"$(cat demo-assets/status-delay.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
    ```

---

## Scenario 2: Finance Automation (Supplier Invoice Routing)

**The Story:** A supplier sends a transport invoice via email. Instead of a human manually entering data into an ERP, the system automatically parses it.

*   **Input Asset:** `demo-assets/invoice-routing.txt`
*   **AI Logic:** The system categorizes the document as an `Invoice`, extracts the `companyName`, `date`, and `totalAmount`.
*   **Action:** The structured JSON is sent to the Finance department's email. The `category: Invoice` flag allows the downstream system to automatically route it to the "Accounts Payable" queue.
*   **Trigger Command:**
    ```bash
    curl -X POST "http://localhost:8080/api/send/ai/email?to=finance@example.com" \
         -H "Content-Type: application/json" \
         -d "{\"text\": \"$(cat demo-assets/invoice-routing.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
    ```

---

## Scenario 3: Operations Digest (Daily Log Insights)

**The Story:** An operations manager receives a messy daily log with various updates. They need a quick overview of what needs attention.

*   **Input Asset:** `demo-assets/logistics-summary.txt`
*   **AI Logic:** The system processes the mixed updates, identifying completed tasks vs. blocked routes (Northern Zone blockage).
*   **Action:** The system generates a structured JSON summary, highlighting the `PENDING` status caused by flooding.
*   **Trigger Command:**
    ```bash
    curl -X POST http://localhost:8080/api/send/ai/extract \
         -H "Content-Type: application/json" \
         -d "{\"text\": \"$(cat demo-assets/logistics-summary.txt | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/\"/\\\"/g')\"}"
    ```

---

## How to Run the Demo

1.  Start the application: `mvn spring-boot:run`
2.  Ensure your `.env` file has valid `GROQ_API_KEY`, `EMAIL_USERNAME/PASSWORD`, and `SLACK_WEBHOOK_URL`.
3.  Execute the cURL commands above to see the automation in action.
4.  Open **Swagger UI** for an interactive experience: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
