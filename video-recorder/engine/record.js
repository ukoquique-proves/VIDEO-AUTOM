const { chromium } = require('playwright');
const path = require('path');
const fs = require('fs');
const { getScenario } = require('./utils');

(async () => {
    const scenario = getScenario();
    const { baseUrl } = scenario.config;
    const { width, height, outputFileName } = scenario.config.video;
    const videoDir = path.join(scenario.__baseDir, 'videos');

    if (!fs.existsSync(videoDir)) {
        fs.mkdirSync(videoDir, { recursive: true });
    }

    console.log(`🚀 Starting recording for scenario: ${baseUrl}`);
    const browser = await chromium.launch({ headless: true });

    const context = await browser.newContext({
        recordVideo: {
            dir: videoDir,
            size: { width, height }
        },
        viewport: { width, height }
    });

    const page = await context.newPage();
    page.on('dialog', async dialog => {
        console.log(`Accepted dialog: ${dialog.message()}`);
        await dialog.accept();
    });

    const metadataPath = path.join(videoDir, 'metadata.json');
    const sceneMetadata = [];

    try {
        let currentVideoTime = 0;
        for (const scene of scenario.scenes) {
            console.log(`--- Scene: ${scene.id} (${scene.duration}s) ---`);
            const sceneStart = Date.now();
            
            // Record actual start time relative to video start
            sceneMetadata.push({
                id: scene.id,
                startTime: currentVideoTime,
                duration: scene.duration
            });

            if (scene.actions) {
                await scene.actions(page);
            }

            const elapsed = (Date.now() - sceneStart) / 1000;
            const remaining = scene.duration - elapsed;
            
            if (remaining > 0) {
                // We still use a timeout here to fulfill the scene duration requirement
                await page.waitForTimeout(remaining * 1000);
                currentVideoTime += scene.duration;
            } else {
                console.warn(`⚠️ Warning: Scene ${scene.id} took ${elapsed.toFixed(2)}s, exceeding duration of ${scene.duration}s`);
                currentVideoTime += elapsed;
            }
        }
        
    } catch (error) {
        console.error('❌ Script failed:', error);
        await page.screenshot({ path: path.join(videoDir, 'error-screenshot.png') });
    } finally {
        // Save metadata even on failure to allow partial sync
        if (sceneMetadata.length > 0) {
            fs.writeFileSync(metadataPath, JSON.stringify(sceneMetadata, null, 2));
            console.log(`📊 Metadata saved: ${metadataPath}`);
        }

        const video = page.video();
        await context.close(); 
        await browser.close();

        if (video) {
            const videoPath = await video.path();
            const targetPath = path.join(videoDir, outputFileName);
            
            // Post-recording file-size check
            const stats = fs.statSync(videoPath);
            const fileSizeKB = stats.size / 1024;
            
            if (fileSizeKB < 500) {
                console.error(`❌ ERROR: Recorded video is too small (${fileSizeKB.toFixed(2)} KB). Capture likely failed.`);
                process.exit(1);
            }

            if (fs.existsSync(targetPath)) fs.unlinkSync(targetPath);
            fs.renameSync(videoPath, targetPath);
            console.log(`✅ Raw video saved: ${targetPath} (${fileSizeKB.toFixed(2)} KB)`);
        }
    }
})();
