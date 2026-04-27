# video-recorder

Standalone Node.js pipeline for generating the AI Logistics Hub promotional video with narration.

---

## ⚠️ Prerequisites — Read Before Running

The pipeline has **two hard requirements** that must be satisfied before running any script:

### 1. Backend must be running with the `demo` Spring profile

`record.js` connects to `http://localhost:8080` to record the dashboard and Swagger UI.  
The **`demo` Spring profile** must be active so that `/api/demo/reset` and `/api/demo/populate` are available.

```bash
# From the project root — start the backend with the demo profile
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

> `record.js` performs a pre-flight check against `GET /api/demo/health` and aborts with a clear error if the server is down or the profile is inactive.

### 2. FFmpeg must be installed

`merge_video_audio.js` invokes `ffmpeg` and `ffprobe` as system commands.

```bash
# Ubuntu / Debian
sudo apt install ffmpeg

# macOS (Homebrew)
brew install ffmpeg
```

---

## 📦 Install Dependencies

Run this once inside the `video-recorder/` directory:

```bash
npm install
npx playwright install chromium
```

---

## 🚀 Running the Pipeline

### Full pipeline (audio → record → merge)

```bash
npm run build-video
```

> **Requires the backend to be running first.** See Prerequisites above.

### Individual steps

| Script | Command | Backend needed? |
|---|---|---|
| Generate narration MP3s | `npm run generate-audio` | ❌ No |
| Record the Playwright video | `npm run record` | ✅ Yes |
| Merge video + audio with FFmpeg | `npm run merge` | ❌ No |

---

## 📁 Output

| File | Description |
|---|---|
| `videos/audio_blocks/block_*.mp3` | Generated TTS narration clips |
| `videos/final_showcase.webm` | Raw Playwright recording (no audio) |
| `videos/final_showcase_with_audio.webm` | **Final output** — video with narration |

> These files are gitignored and will not be committed.
