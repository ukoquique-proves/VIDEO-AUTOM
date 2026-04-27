const { chromium } = require('playwright');
const path = require('path');
const fs = require('fs');

async function checkServer() {
    console.log('Checking if backend server is running on http://localhost:8080 ...');
    try {
        const response = await fetch('http://localhost:8080/');
        if (!response.ok) return false;
        console.log('Server is up and running!');
    } catch (e) {
        return false;
    }

    // Also verify the demo profile is active — DemoController is @Profile("demo") gated.
    // Use GET /api/demo/health (a dedicated endpoint) instead of HEAD against a POST endpoint,
    // which would return 405 Method Not Allowed even when the profile IS active.
    console.log('Checking if "demo" Spring profile is active...');
    try {
        const demoCheck = await fetch('http://localhost:8080/api/demo/health');
        if (demoCheck.status === 404) {
            console.error('\n❌ ERROR: /api/demo/health returned 404. The Spring "demo" profile is NOT active.');
            console.error('Start the server with: mvn spring-boot:run -Dspring-boot.run.profiles=demo');
            return false;
        }
    } catch (e) {
        return false;
    }
    console.log('Demo profile confirmed active.');
    return true;
}

(async () => {
    const isServerUp = await checkServer();
    if (!isServerUp) {
        console.error('\n❌ ERROR: The backend server is NOT running or demo profile is inactive at http://localhost:8080.');
        console.error('Please start the Spring Boot application with: mvn spring-boot:run -Dspring-boot.run.profiles=demo');
        process.exit(1);
    }

    console.log('Starting Playwright script...');
    const browser = await chromium.launch({ headless: true });

    // We will save the video directly to the system root as requested or similar folder
    const videoDir = path.join(__dirname, 'videos');
    if (!fs.existsSync(videoDir)) {
        fs.mkdirSync(videoDir);
    }

    const context = await browser.newContext({
        recordVideo: {
            dir: videoDir,
            size: { width: 1280, height: 720 }
        },
        viewport: { width: 1280, height: 720 }
    });

    const page = await context.newPage();

    // Accept dialogs automatically
    page.on('dialog', async dialog => {
        console.log(`Accepted dialog: ${dialog.message()}`);
        await dialog.accept();
    });

    try {
        console.log('Navigating to dashboard...');
        await page.goto('http://localhost:8080/', { waitUntil: 'load' });

        console.log('Clicking Reset Database...');
        await page.evaluate(() => document.getElementById('btnReset').click());
        // Wait holding the clean state for 10 seconds (Matches audio block 00 to 10)
        await page.waitForTimeout(10000);

        console.log('Clicking Run Demo Scenarios...');
        await page.evaluate(() => document.getElementById('btnDemo').click());

        // Wait for table to populate
        console.log('Waiting for records to populate...');
        await page.waitForTimeout(8000); // Give it time to finish rendering, holding Scene 2

        console.log('Hovering over an urgent badge...');
        try {
            await page.locator('.badge-urgent').first().hover({ timeout: 5000 });
        } catch (e) {
            console.log("Could not hover over urgent badge, continuing...");
        }
        await page.waitForTimeout(3000);

        // This reaches ~21 seconds. Add wait to hit 25.
        await page.waitForTimeout(4000);

        console.log('Navigating to Swagger UI...');
        await page.goto('http://localhost:8080/swagger-ui/index.html', { waitUntil: 'load' });
        await page.waitForTimeout(5000);

        console.log('Interacting with Swagger UI...');
        // Wait for Swagger to load
        await page.waitForSelector('.swagger-ui', { timeout: 30000 });

        // Expand the extract operation (/api/send/ai/extract)
        console.log('Waiting for operation to be visible...');
        const opblockHeader = page.locator('.opblock-summary-path:has-text("/api/send/ai/extract")').first();
        await opblockHeader.waitFor({ state: 'visible', timeout: 30000 });
        console.log('Clicking operation block...');
        await opblockHeader.click();
        await page.waitForTimeout(2000);

        // Click "Try it out"
        console.log('Looking for Try it out button...');
        const tryOutBtn = page.locator('.try-out button');
        await tryOutBtn.waitFor({ state: 'visible', timeout: 30000 });
        await tryOutBtn.click();
        await page.waitForTimeout(1000);

        const customText = "URGENT: CRITICAL Port Blockage at Rotterdam. Shipment #AX-99 blocked by strike. Rescue cost estimate: $125,000. Status: DELAYED.";

        // Fill in the body (the textarea for application/json)
        await page.fill('textarea.body-param__text', `{\n  "text": "${customText}"\n}`);
        await page.waitForTimeout(1000);

        // Click Execute
        const executeBtn = page.getByRole('button', { name: /Execute/i });
        await executeBtn.click();

        // Wait for response status 200
        await page.waitForSelector('.response-col_status:has-text("200")', { timeout: 15000 });
        // Hold swagger UI for a few seconds to let user read
        await page.waitForTimeout(5000);

        console.log('Navigating back to dashboard...');
        await page.goto('http://localhost:8080/', { waitUntil: 'load' });

        // Wait for records to appear and pause for Integration Proof
        await page.waitForTimeout(10000); // Hold for audio block at 45

        console.log('Scrolling up and down...');
        await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
        await page.waitForTimeout(2000);
        await page.evaluate(() => window.scrollTo(0, 0));
        await page.waitForTimeout(2000);

        // Ensure we hit the 60 second mark
        await page.waitForTimeout(5000);

        console.log('Finished operations. Closing context to save video...');
    } catch (error) {
        console.error('An error occurred during the script execution:', error);
        // Take a screenshot on failure to help debug
        await page.screenshot({ path: path.join(__dirname, 'error-screenshot.png') });
    } finally {
        const video = page.video();
        await context.close(); // Ensures video is saved completely
        await browser.close();

        if (video) {
            const videoPath = await video.path();
            const targetPath = path.join(videoDir, 'final_showcase.webm');
            if (fs.existsSync(targetPath)) {
                fs.unlinkSync(targetPath);
            }
            fs.renameSync(videoPath, targetPath);
            console.log('Expected video file saved as final_showcase.webm in', videoDir);
        } else {
            console.log('Browser closed. Video object not found in Playwright page.');
        }
    }
})();
