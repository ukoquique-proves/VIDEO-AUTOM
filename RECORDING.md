# Video Production Workflow: AI Logistics Hub

This document outlines the automated recording sequence for the promotional video. Antigravity will execute these steps using the browser subagent to produce a high-quality video artifact.

## 🎬 Recording Sequence

### Scene 1: The Clean Slate (0:00 - 0:10)
1. **Navigate** to `http://localhost:8080`.
2. **Action**: Ensure the dashboard is in a "Clean State" (0 records). If records exist, click **"Reset Database"** first.
3. **Outcome**: Visual of the empty, glassmorphic dashboard with the "Live Monitoring Active" pulse.

### Scene 2: The Moment of Production (0:10 - 0:25)
1. **Action**: Click the **"Run Demo Scenarios"** button.
2. **Wait**: Allow 5-10 seconds for the AI to process all 5 files from `demo-assets`.
3. **Outcome**: Table populates live with IDs #1 through #5.
4. **Highlight**: Hover over an **URGENT** badge (e.g., Red status) to show interactivity.

### Scene 3: The Custom Extraction (0:25 - 0:45)
1. **Navigate** to `http://localhost:8080/swagger-ui/index.html`.
2. **Action**: Find `POST /api/send/ai/extract` in the "Sending Operations" section.
3. **Input**: Click "Try it out" and paste the following text:
   ```text
   URGENT: Shipment from Global Logistics Inc is stuck at Rotterdam Port due to a custom strike. Total value of delayed goods is $89,400. Status: CRITICAL DELAY.
   ```
4. **Execute**: Click the big blue button.
5. **Outcome**: Show the structured JSON response appearing instantly in the Swagger console.

### Scene 4: The Integration Proof (0:45 - 0:55)
1. **Navigate** back to the Dashboard.
2. **Action**: Show record #6 appearing in the list.
3. **Commentary**: Mentions that Slack notifications have been dispatched automatically (as per `AIService` logic).

### Scene 5: Technical Excellence (0:55 - 1:00)
1. **Naviate**: Briefly scroll through the `AIService.java` or `ExtractionService.java` in the IDE to show clean, professional Java code.

## 🛠️ Recording Configuration
- **Recording Name**: `ai_logistics_hub_showcase`
- **Resolution**: Standard browser viewport.
- **Duration**: Target ~60 seconds.

---
**Note to Model**: Run these steps in a single `browser_subagent` session if possible, or sequential steps to ensure one cohesive `webp` recording.

## 🤖 Current Automation Approach

Since the browser subagent recordings were taking too long and experiencing timeouts attempting a single cohesive take, we are currently generating the recording via a **Node.js Playwright script**. 

This script natively connects to `localhost:8080`, executes the requested interactions exactly as outlined above, and leverages Playwright's built-in `recordVideo` functionality to reliably export a `.webm` file into the `video-recorder/videos` directory.

### ⚠️ The "Sound & Narrative" Problem
The automated Playwright approach has two major limitations for a promotional showcase:
1. **No Audio**: The generated clips are completely silent.
2. **Technical Pacing**: The script executes interactions with technical precision but lacks the natural pacing and "storytelling" feel of a manual demo.

### 🎙️ Automatización de Audio (Generación y Mezcla)
Para evitar procesos manuales, hemos implementado una pipeline de audio:
1. **Generación (TTS)**: `video-recorder/generate_audio.js` crea los archivos `.mp3` desde el guion.
2. **Mezcla Inteligente**: `video-recorder/merge_video_audio.js` utiliza **FFmpeg** para:
    - **Sincronizar**: Inyecta los audios en los tiempos exactos del video.
    - **Evitar Solapamientos**: Utiliza el filtro `atempo` para acelerar automáticamente los fragmentos de voz que duran más que su ranura de tiempo asignada.
    - **Resultado**: Un video final `final_showcase_with_audio.webm` con narrativa fluida y sin voces superpuestas.
