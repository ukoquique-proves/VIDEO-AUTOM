# PLAN: Video Automation Platform Scaling

This plan outlines the evolution of the current tooling into a complete, robust SaaS-like application for generating software demo videos.

## Phase 1: Debugging & Stabilization (Immediate)

The recent 2-minute video highlighted critical synchronization and reliability issues.

1.  **Frame-Perfect Sync Engine**:
    -   Modify `engine/record.js` to emit "scene-start" events to a side-car log.
    -   Update `engine/merge_video_audio.js` to read this log instead of assuming fixed `startTime` offsets.
2.  **Dynamic Visual Feedback**:
    -   Implement an overlay/watermark during recording that shows the current Scene ID. This helps debug exactly where visuals and audio drift apart.
3.  **Advanced Wait Strategies**:
    -   Replace all remaining `waitForTimeout` with `waitForFunction` or `waitForSelector` to handle variable network/API latency (Groq, Swagger, etc.).
4.  **Audio Length Safety**:
    -   Add a pre-merge check that uses `ffprobe` to ensure `audio_duration <= scene_duration`. If audio is longer, automatically extend the visual scene in the recording or fail the build.

## Phase 2: Core Platform Features

Transform the "scripts" into a "platform".

1.  **Multi-Scenario UI**:
    -   Create a simple React/Next.js dashboard to trigger recordings, manage scenarios, and preview generated videos.
2.  **Scenario Editor**:
    -   A JSON-based or Visual editor to define scenes, actions, and narration without touching `.js` files.
3.  **Cloud Rendering**:
    -   Dockerize the entire engine (Playwright + FFmpeg) to run in headless environments (AWS Lambda, GitHub Actions).
4.  **Variable Injection**:
    -   Support environment variable injection into scenarios (e.g., pass different `baseUrl` or `apiKey` at runtime).

## Phase 3: High-Quality Production

Moving beyond "mechanical" demos.

1.  **Professional TTS Integration**:
    -   Migrate from `gTTS` to **ElevenLabs** or **Azure Speech Service** for human-like narration.
2.  **Visual Polish**:
    -   Automated mouse cursor smoothing (mimicking natural movement).
    -   Automatic zooming/panning on active UI elements.
    -   Intro/Outro branded overlays.
3.  **Audio Post-Processing**:
    -   Add background music ducking (music volume lowers when narration starts).
    -   Noise reduction and equalization for TTS output.

## Phase 4: Scaling & Integration

1.  **GitHub Action Integration**:
    -   A "Video-as-Code" workflow where a PR triggers a new demo video of the feature.
2.  **Multi-Language Support**:
    -   Automatic translation of `audioText` and generation of videos in multiple languages.
3.  **Plugin System**:
    -   Allow custom "Action Hooks" (e.g., "capture-network-log", "mock-api-response").

---

## Technical Debt to Resolve
-   **Error Handling**: The current `process.exit(1)` is too blunt for a web app; need structured error reporting.
-   **Clean-up Logic**: Automated deletion of temporary audio blocks after a successful merge.
-   **Browser Resilience**: Handle "Tab Crashed" or "Navigation Timeout" gracefully.
