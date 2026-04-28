# General-Purpose Video Automation Pipeline

## Goal
Evolve the current `video-recorder/` tooling from a logistics-hub-specific script into a **reusable video production engine** that can generate narrated demo videos for any web application.

---

## Architecture Overview

```
video-recorder/
├── engine/                     ← Generic pipeline (project-agnostic)
│   ├── record.js               ← Scenario-driven Playwright recorder
│   ├── generate_audio.js       ← TTS generator from scenario.audioText
│   ├── merge_video_audio.js    ← FFmpeg audio-video synchronizer
│   └── utils.js                ← Shared helpers
│
├── scenarios/                  ← One subfolder per project
│   ├── logistics-hub/          ← Existing AI Logistics Hub demo
│   │   ├── scenario.js         ← Scene definitions, URLs, narration
│   │   └── videos/             ← Generated artifacts for this scenario
│   │
│   └── _template/              ← Starter template for new projects
│       └── scenario.js         ← Minimal scenario template
│
├── package.json
└── README.md
```

### Key Principle
**The engine knows nothing about the target project.** All project-specific details (URLs, CSS selectors, narration text, input payloads) live exclusively in `scenarios/<project>/scenario.js`.

---

## � Running the Pipeline

To generate a video, you must specify the scenario file path using the `--scenario` argument.

### Full build (audio + record + merge)
```bash
npm run build-video --scenario=scenarios/logistics-hub/scenario.js
```

### Individual Steps
| Script | Command |
|---|---|
| Generate narration | `npm run generate-audio --scenario=...` |
| Record video | `npm run record --scenario=...` |
| Merge audio/video | `npm run merge --scenario=...` |

---

## �️ Creating a New Scenario

1. **Copy the template**: `cp -r scenarios/_template scenarios/my-new-project`
2. **Edit `scenario.js`**: Define your `baseUrl`, scenes, durations, and Playwright actions.
3. **Run the build**: `npm run build-video --scenario=scenarios/my-new-project/scenario.js`

---

## 📁 Output Artifacts
All generated files are stored within the project's specific folder:
`scenarios/<project>/videos/`

- `audio_blocks/`: Individual TTS clips.
- `raw_recording.webm`: Raw Playwright capture.
- `final_video.webm`: Final narrated showcase.

---

## ⚠️ Prerequisites
1. **Node.js Dependencies**: Run `npm install` and `npx playwright install chromium`.
2. **FFmpeg**: Must be installed on the system (`sudo apt install ffmpeg`).
3. **Application State**: Ensure the target application is running and accessible at the `baseUrl` defined in the scenario.
