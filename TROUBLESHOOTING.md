# Troubleshooting

## Docker Build Fails: `COPY target/*.jar` not found

**Symptom:** `docker build` exits with an error like:

```
COPY failed: file not found in build context
```

**Root Cause:** The `Dockerfile` referenced a hardcoded, versioned JAR name that did not match Maven's actual output.

| File | Wrong | Correct |
|------|-------|---------|
| `Dockerfile` | `COPY target/apibridge-0.0.1-SNAPSHOT.jar app.jar` | `COPY target/autom-hub-0.0.1-SNAPSHOT.jar app.jar` |

Maven uses the `<artifactId>` from `pom.xml` (`autom-hub`), not the group id, when naming the built JAR.

**Fix:** Always verify the exact JAR name in `target/` before copying:

```bash
ls target/*.jar
```

Then update the `COPY` line in `Dockerfile` to match the exact filename.

---

## `Paths.get("demo-assets")` silently fails inside Docker

**Symptom:** `POST /api/demo/populate` returns `200` but the dashboard stays empty, with no visible error.

**Root Cause:** `Paths.get("demo-assets")` resolves relative to the JVM working directory (`WORKDIR` in the Dockerfile). If `demo-assets/` is not copied into the image, the directory does not exist and the endpoint returns `"demo-assets directory not found"` — but only as an `HTTP 500`, which the old UI silently ignored.

**Fix:** Ensure `demo-assets/` is copied into the container at the same path the app expects:

```dockerfile
WORKDIR /app
COPY target/autom-hub-0.0.1-SNAPSHOT.jar app.jar
COPY demo-assets ./demo-assets   # <-- required for /api/demo/populate
```

---

## `/api/demo/populate` returns `200` but nothing is saved

**Symptom:** The "Run Demo Scenarios" button shows a success popup but the dashboard remains empty.

**Root Cause:** The original endpoint swallowed per-file exceptions and always returned `HTTP 200` even when zero records were created. This gave the false impression of success.

**Fix:** The endpoint now:
- Counts successful and failed records separately.
- Returns `HTTP 500` when `0` records are created.
- Returns a descriptive body: `"No demo records were created. First error: <reason>"`.

The UI now reads and displays this backend message and always refreshes the table after calling populate.

---

## `401 Unauthorized` from Groq API in Docker

**Symptom:** `/api/demo/populate` returns `500` with `401 Unauthorized`.

**Root Cause:** Environment variables (`GROQ_API_KEY`, `EMAIL_USERNAME`, etc.) are not passed into the container.

**Fix:** Pass env vars at runtime:

```bash
docker run -d \
  -p 8080:8080 \
  -e GROQ_API_KEY=your_groq_key \
  -e EMAIL_USERNAME=your_email@gmail.com \
  -e EMAIL_PASSWORD=your_16_char_app_password \
  -e SLACK_WEBHOOK_URL=https://hooks.slack.com/services/YOUR/WEBHOOK/URL \
  autom-hub:local
```

Or use a `.env` file with `--env-file`:

```bash
docker run -d -p 8080:8080 --env-file .env autom-hub:local
```

---

## Port `8080` already in use

**Symptom:** `BUILD FAILURE` with:

```
Web server failed to start. Port 8080 was already in use.
```

**Fix:** Stop the existing process or run the container on a different port:

```bash
# Find and kill the process on 8080
lsof -ti:8080 | xargs kill -9

# Or, remap the container port
docker run -d -p 9090:8080 autom-hub:local
# Then open http://localhost:9090
```

---

## `mvn` command fails with `MissingProjectException`

**Symptom:**

```
The goal you specified requires a project to execute
but there is no POM in this directory
```

**Root Cause:** Maven was invoked from the wrong directory (e.g., `/root` instead of the project root).

**Fix:** Always `cd` into the project directory first:

```bash
cd ai-logistics-automation-hub-main
mvn -DskipTests package
```

---

## General Debugging Checklist

If demo populate is not working, verify in this order:

1. **Does the JAR exist?** `ls target/*.jar`
2. **Are env vars set?** `echo $GROQ_API_KEY` (must not be empty)
3. **Does Docker image build cleanly?** `docker build -t autom-hub:local .`
4. **Are demo assets in the image?** `docker run --rm autom-hub:local ls app/demo-assets/`
5. **Does the API respond?** `curl http://localhost:8080/api/extractions`
6. **Does populate return an accurate response?** `curl -X POST http://localhost:8080/api/demo/populate`
