/**
 * 2-Minute Extended Scenario Configuration
 * Extended version of the logistics-hub demo with additional scenes.
 */
module.exports = {
    config: {
        baseUrl: 'http://localhost:8080',
        video: {
            width: 1280,
            height: 720,
            fps: 30,
            outputFileName: 'final_showcase_2min.webm',
            finalOutputFileName: 'final_showcase_2min_with_audio.webm'
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
            audioText: "Logistics data is often trapped in messy emails and invoices. Today, we automate that chaos with the AI Logistics Hub.",
            actions: async (page) => {
                console.log('Navigating to dashboard...');
                await page.goto('http://localhost:8080/', { waitUntil: 'load' });
                console.log('Clicking Reset Database...');
                await page.evaluate(() => document.getElementById('btnReset').click());
            },
            duration: 15
        },
        {
            id: 'demo_populate',
            startTime: 15,
            audioText: "This is the AI Logistics Hub. Watch as our Groq-powered pipeline extracts structured data from 5 real-world logistics scenarios, including supplier invoices, shipment delays, and operational summaries.",
            actions: async (page) => {
                console.log('Clicking Run Demo Scenarios...');
                await page.evaluate(() => document.getElementById('btnDemo').click());
                console.log('Waiting for records to populate...');
                // Wait for at least one record, then wait a bit more for the rest
                await page.waitForSelector('table tbody tr:nth-child(1)', { timeout: 30000 });
                await page.waitForTimeout(10000); // Wait for the rest to finish processing
            },
            duration: 20
        },
        {
            id: 'interactivity',
            startTime: 35,
            audioText: "The system instantly identifies companies, dates, amounts, and categorizes each record by urgency. Color-coded badges provide instant operational visibility: red for urgent, orange for delayed, green for delivered.",
            actions: async (page) => {
                console.log('Hovering over an urgent badge...');
                try {
                    await page.locator('.badge-urgent').first().hover({ timeout: 5000 });
                    await page.waitForTimeout(2000);
                } catch (e) {
                    console.log("Could not hover over urgent badge, continuing...");
                }
            },
            duration: 20
        },
        {
            id: 'swagger_input',
            startTime: 55,
            audioText: "Let's submit a critical event in real-time. A port blockage in Rotterdam affecting shipment AX-99. The AI instantly extracts the 125,000 dollar value at risk, the status, and all relevant details.",
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
            duration: 20
        },
        {
            id: 'swagger_explore',
            startTime: 75,
            audioText: "The Swagger UI provides complete API documentation for all endpoints. Developers can test the extraction service, retrieve stored records, and trigger email or Slack notifications directly from the browser.",
            actions: async (page) => {
                console.log('Exploring Swagger UI...');
                await page.evaluate(() => window.scrollBy(0, 300));
                await page.waitForTimeout(3000);
                await page.evaluate(() => window.scrollBy(0, 300));
                await page.waitForTimeout(3000);
            },
            duration: 20
        },
        {
            id: 'h2_console',
            startTime: 95,
            audioText: "Behind the scenes, H2 database provides persistent storage. All extraction records survive server restarts, ensuring data integrity for production deployments.",
            actions: async (page) => {
                console.log('Navigating to H2 console...');
                await page.goto('http://localhost:8080/h2-console', { waitUntil: 'load' });
                await page.waitForTimeout(3000);
                // Return to dashboard
                await page.goto('http://localhost:8080/', { waitUntil: 'load' });
            },
            duration: 15
        },
        {
            id: 'integration_proof',
            startTime: 110,
            audioText: "The new record appears instantly in the dashboard. Slack alerts and email notifications are dispatched automatically, keeping stakeholders informed in real-time. Professional automation built with Spring Boot 3.",
            actions: async (page) => {
                console.log('Navigating back to dashboard...');
                await page.goto('http://localhost:8080/', { waitUntil: 'load' });
                await page.waitForTimeout(5000);
            },
            duration: 15
        },
        {
            id: 'closing',
            startTime: 125,
            audioText: "Built for scale, designed for reliability. The AI Logistics Hub transforms unstructured logistics data into actionable insights. Automate your workflow today.",
            actions: async (page) => {
                console.log('Final scroll...');
                await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
                await page.waitForTimeout(3000);
                await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight / 2));
                await page.waitForTimeout(3000);
                await page.evaluate(() => window.scrollTo(0, 0));
            },
            duration: 15
        }
    ]
};
