/**
 * Centralized Scenario Configuration
 * Decouples the video production pipeline from the project-specific logic.
 */
module.exports = {
    config: {
        baseUrl: 'http://localhost:8080',
        video: {
            width: 1280,
            height: 720,
            fps: 30,
            outputFileName: 'final_showcase.webm',
            finalOutputFileName: 'final_showcase_with_audio.webm'
        },
        audio: {
            language: 'en',
            outputDir: 'audio_blocks'
        }
    },
    scenes: [
        {
            id: 'clean_slate',
            startTime: 0,
            audioText: "Logistics data is often trapped in messy emails and invoices. Today, we automate that chaos.",
            actions: async (page) => {
                console.log('Navigating to dashboard...');
                await page.goto('http://localhost:8080/', { waitUntil: 'load' });
                console.log('Clicking Reset Database...');
                await page.evaluate(() => document.getElementById('btnReset').click());
            },
            duration: 10
        },
        {
            id: 'demo_populate',
            startTime: 10,
            audioText: "This is the AI Logistics Hub. Watch as our Groq-powered pipeline extracts data from 5 scenarios live.",
            actions: async (page) => {
                console.log('Clicking Run Demo Scenarios...');
                await page.evaluate(() => document.getElementById('btnDemo').click());
                console.log('Waiting for 5 records to populate...');
                await page.waitForSelector('table tbody tr:nth-child(5)', { timeout: 30000 });
            },
            duration: 10
        },
        {
            id: 'interactivity',
            startTime: 20,
            audioText: "The system identifies companies, dates, and amounts, using color-coded badges for instant operational visibility.",
            actions: async (page) => {
                console.log('Hovering over an urgent badge...');
                try {
                    await page.locator('.badge-urgent').first().hover({ timeout: 5000 });
                } catch (e) {
                    console.log("Could not hover over urgent badge, continuing...");
                }
            },
            duration: 10
        },
        {
            id: 'swagger_input',
            startTime: 30,
            audioText: "Let's submit a critical event. A port blockage in Rotterdam. The AI instantly identifies the 125,000 dollar value at risk.",
            actions: async (page) => {
                console.log('Navigating to Swagger UI...');
                await page.goto('http://localhost:8080/swagger-ui/index.html', { waitUntil: 'load' });
                await page.waitForSelector('.swagger-ui', { timeout: 30000 });

                const path = "/api/send/ai/extract";
                const opblockHeader = page.locator(`.opblock-summary-path:has-text("${path}")`).first();
                await opblockHeader.waitFor({ state: 'visible', timeout: 30000 });
                await opblockHeader.click();
                await page.waitForTimeout(2000);
                
                const tryOutBtn = page.locator('.try-out button');
                await tryOutBtn.waitFor({ state: 'visible', timeout: 30000 });
                await tryOutBtn.click();
                await page.waitForTimeout(1000);

                const customText = "URGENT: CRITICAL Port Blockage at Rotterdam. Shipment #AX-99 blocked by strike. Rescue cost estimate: $125,000. Status: DELAYED.";
                await page.fill('textarea.body-param__text', JSON.stringify({ text: customText }, null, 2));
                
                const executeBtn = page.getByRole('button', { name: /Execute/i });
                await executeBtn.click();
                await page.waitForSelector('.response-col_status:has-text("200")', { timeout: 15000 });
            },
            duration: 15
        },
        {
            id: 'integration_proof',
            startTime: 45,
            audioText: "The record appears instantly, and Slack alerts are dispatched. Professional automation built with Spring Boot 3.",
            actions: async (page) => {
                console.log('Navigating back to dashboard...');
                await page.goto('http://localhost:8080/', { waitUntil: 'load' });
                await page.waitForTimeout(5000);
            },
            duration: 10
        },
        {
            id: 'closing',
            startTime: 55,
            audioText: "Built for scale. AI Logistics Hub.",
            actions: async (page) => {
                console.log('Final scroll...');
                await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
                await page.waitForTimeout(2000);
                await page.evaluate(() => window.scrollTo(0, 0));
            },
            duration: 5
        }
    ]
};
