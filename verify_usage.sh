#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api"

echo "=== API Bridge Usage Verification ==="

# 1. Send Existing Extraction to Email
# Requires an ID that exists in the DB. Since we use H2 mem, we might need to seed it first or this will 404.
echo "1. Sending Extraction #1 to Email..."
curl -X POST "$BASE_URL/send/email/1?to=test@example.com" -v

# 2. Extract and Send via AI (Email)
echo -e "\n\n2. AI Extraction -> Email..."
curl -X POST "$BASE_URL/send/ai/email?to=test@example.com" \
     -H "Content-Type: application/json" \
     -d '{"text": "Invoice from ACME Corp for $500.00 on 2023-12-01"}' -v

# 3. Extract and Send via AI (Slack)
echo -e "\n\n3. AI Extraction -> Slack..."
curl -X POST "$BASE_URL/send/ai/slack" \
     -H "Content-Type: application/json" \
     -d '{"text": "Urgent payment required for Server Hosting: $99.99 due tomorrow"}' -v

echo -e "\n\n=== Done ==="
