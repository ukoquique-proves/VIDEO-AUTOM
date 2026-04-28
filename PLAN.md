# PLAN: General-Purpose Video Automation Pipeline

## Goal

Evolve the current `video-recorder/` tooling from a logistics-hub-specific script into a **reusable video production engine** that can generate narrated demo videos for any web application, while preserving the existing AI Logistics Hub showcase as one scenario.

---

## Architecture Overview

```
video-recorder/
├── engine/                     ← Generic pipeline (project-agnostic)
│   ├── record.js               ← Scenario-driven Playwright recorder
│   ├── generate_audio.js       ← TTS generator from scenario.audioText
│   ├── merge_video_audio.js    ← FFmpeg audio-video synchronizer
│   └── utils.js                ← Shared helpers (server check, duration probe)
│
├── scenarios/                  ← One subfolder per project
│   ├── logistics-hub/          ← Existing AI Logistics Hub demo
│   │   ├── scenario.js         ← Scene definitions, URLs, narration
│   │   └── videos/             ← Generated artifacts for this scenario
│   │       ├── audio_blocks/
│   │       ├── final_showcase.webm
│   │       └── final_showcase_with_audio.webm
│   │
│   └── _template/              ← Starter template for new projects
│       └── scenario.js         ← Minimal scenario with placeholder scenes
│
├── package.json
└── README.md
```

### Key Principle

**The engine knows nothing about the target project.** All project-specific details (URLs, CSS selectors, narration text, input payloads) live exclusively in `scenarios/<project>/scenario.js`.

---

## Step-by-Step Migration

### Step 1: Extract engine files

Move the three pipeline scripts into `engine/`, stripping all hardcoded project references:

- **`engine/record.js`** — Accept a scenario path as a CLI argument (`--scenario`). Load the scenario dynamically with `require()`. All `baseUrl`, selectors, and actions come from the scenario.
- **`engine/generate_audio.js`** — Accept a scenario path. Read `scenes[].audioText` and `config.audio` from the scenario. Output audio blocks into `scenarios/<project>/videos/audio_blocks/`.
- **`engine/merge_video_audio.js`** — Accept a scenario path. Read `scenes[].startTime` and `config.video` from the scenario. Merge into `scenarios/<project>/videos/`.
- **`engine/utils.js`** — Shared functions: `checkServer(baseUrl)`, `getVideoDuration(filePath)`, `ensureDir(dir)`.

### Step 2: Relocate the logistics-hub scenario

Move the current `scenario.js` into `scenarios/logistics-hub/scenario.js`. Update all hardcoded `http://localhost:8080` references to use `config.baseUrl` (already done for most). Ensure the `videos/` output directory resolves to `scenarios/logistics-hub/videos/`.

### Step 3: Create the scenario template

Add `scenarios/_template/scenario.js` with a minimal 2-scene structure and inline comments explaining each field:

```js
module.exports = {
    config: {
        baseUrl: 'http://localhost:XXXX',
        video: { width: 1280, height: 720, fps: 30, outputFileName: 'demo.webm', finalOutputFileName: 'demo_with_audio.webm' },
        audio: { language: 'en', outputDir: 'audio_blocks' }
    },
    scenes: [
        {
            id: 'intro',
            startTime: 0,
            audioText: 'Welcome to the demo.',
            actions: async (page) => {
                await page.goto('http://localhost:XXXX/', { waitUntil: 'load' });
            },
            duration: 10
        },
        {
            id: 'closing',
            startTime: 10,
            audioText: 'Thank you for watching.',
            actions: async (page) => {
                await page.evaluate(() => window.scrollTo(0, 0));
            },
            duration: 5
        }
    ]
};
```

### Step 4: Update npm scripts in package.json

Replace the current hardcoded scripts with scenario-aware commands:

```json
{
  "scripts": {
    "generate-audio": "node engine/generate_audio.js --scenario $npm_config_scenario",
    "record": "node engine/record.js --scenario $npm_config_scenario",
    "merge": "node engine/merge_video_audio.js --scenario $npm_config_scenario",
    "build-video": "npm run generate-audio && npm run record && npm run merge"
  }
}
```

Usage:
```bash
npm run build-video --scenario=scenarios/logistics-hub
npm run build-video --scenario=scenarios/my-other-project
```

### Step 5: Update .gitignore

```
# Generated artifacts per scenario
scenarios/*/videos/
```

This replaces the current `video-recorder/videos/` rule and covers all future scenarios.

### Step 6: Update documentation

- **`video-recorder/README.md`** — Rewrite to explain the generic engine + scenario model, how to create a new scenario, and link to `scenarios/logistics-hub/` as a worked example.
- **`RECORDING.md`** — Update paths to reflect `scenarios/logistics-hub/`. Keep the scene descriptions as-is since they describe the logistics hub demo.
- **`docs/roadmap.md`** — Add a new phase for "Pipeline Generalization".

---

## Section: AI Logistics Hub Video (Preserved)

The existing showcase video is preserved under `scenarios/logistics-hub/`. No functionality is lost.

### What stays the same
- 6-scene structure with the same timings (0:00–1:00)
- Same narration text and TTS generation
- Same Playwright interactions (dashboard reset, demo populate, Swagger extraction, closing scroll)
- Same FFmpeg merge with `adelay` + `amix`

### What changes (paths only)
| Before | After |
|---|---|
| `video-recorder/scenario.js` | `video-recorder/scenarios/logistics-hub/scenario.js` |
| `video-recorder/videos/` | `video-recorder/scenarios/logistics-hub/videos/` |
| `node record.js` | `node engine/record.js --scenario scenarios/logistics-hub` |
| `node generate_audio.js` | `node engine/generate_audio.js --scenario scenarios/logistics-hub` |
| `node merge_video_audio.js` | `node engine/merge_video_audio.js --scenario scenarios/logistics-hub` |

### Prerequisites (unchanged)
1. Start the backend: `mvn spring-boot:run -Dspring-boot.run.profiles=demo`
2. Install FFmpeg: `sudo apt install ffmpeg`
3. Install Node dependencies: `npm install`

---

## Acceptance Criteria

- [ ] `npm run build-video --scenario=scenarios/logistics-hub` produces the same video as before
- [ ] Copying `_template/` and editing `scenario.js` is sufficient to produce a video for a different project
- [ ] No project-specific URL, selector, or narration text exists outside `scenarios/`
- [ ] `RECORDING.md` accurately references the new paths
- [ ] `.gitignore` covers all scenario output directories
