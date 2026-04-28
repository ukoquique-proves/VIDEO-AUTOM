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

    try {
        for (const scene of scenario.scenes) {
            console.log(`--- Scene: ${scene.id} (${scene.duration}s) ---`);
            const sceneStart = Date.now();
            
            if (scene.actions) {
                await scene.actions(page);
            }

            const elapsed = (Date.now() - sceneStart) / 1000;
            const remaining = scene.duration - elapsed;
            
            if (remaining > 0) {
                await page.waitForTimeout(remaining * 1000);
            }
        }
    } catch (error) {
        console.error('❌ Script failed:', error);
        await page.screenshot({ path: path.join(videoDir, 'error-screenshot.png') });
    } finally {
        const video = page.video();
        await context.close(); 
        await browser.close();

        if (video) {
            const videoPath = await video.path();
            const targetPath = path.join(videoDir, outputFileName);
            if (fs.existsSync(targetPath)) fs.unlinkSync(targetPath);
            fs.renameSync(videoPath, targetPath);
            console.log(`✅ Raw video saved: ${targetPath}`);
        }
    }
})();
