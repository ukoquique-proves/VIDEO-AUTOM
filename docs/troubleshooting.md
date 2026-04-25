# Troubleshooting Guide

This document provides solutions for common issues encountered when building, running, or deploying the AI Logistics Automation Hub.

## Docker Build Fails: `COPY target/*.jar` not found

**Symptom:** `docker build` exits with an error like: `COPY failed: file not found in build context`.

**Root Cause**: The `Dockerfile` referenced a JAR name that did not match Maven's output. Maven uses the `<artifactId>` from `pom.xml` (`autom-hub`) for the filename.

**Fix**: Ensure your `Dockerfile` uses the correct artifact name:
```dockerfile
COPY target/autom-hub-0.0.1-SNAPSHOT.jar app.jar
```

---

## `Paths.get("demo-assets")` fails inside Docker

**Symptom**: `POST /api/demo/populate` returns `200` but no data is loaded.

**Root Cause**: The `demo-assets/` directory was not copied into the Docker image.

**Fix**: Ensure `demo-assets/` is included in your image:
```dockerfile
WORKDIR /app
COPY target/autom-hub-0.0.1-SNAPSHOT.jar app.jar
COPY demo-assets ./demo-assets
```

---

## `401 Unauthorized` from Groq API

**Symptom**: AI extraction fails with a `401` error.

**Root Cause**: Missing or invalid `GROQ_API_KEY`.

**Fix**: Pass environment variables at runtime:
```bash
docker run -d -p 8080:8080 --env-file .env autom-hub:local
```

---

## Port `8080` already in use

**Fix**: Stop the existing process or remap the port:
```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9

# Or remap container port
docker run -d -p 9090:8080 autom-hub:local
```

---

## General Debugging Checklist

1. **Verify Artifact**: `ls target/*.jar`
2. **Check Env Vars**: `echo $GROQ_API_KEY`
3. **Verify API Status**: `curl http://localhost:8080/api/extractions`
4. **Check Logs**: Monitor the application logs for specific exception stack traces.
