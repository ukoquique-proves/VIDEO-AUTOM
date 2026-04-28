/**
 * Scenario Template
 * Use this as a starting point for new video automation projects.
 */
module.exports = {
    config: {
        baseUrl: 'http://localhost:3000', // Update to your app's base URL
        video: {
            width: 1280,
            height: 720,
            fps: 30,
            outputFileName: 'raw_recording.webm',
            finalOutputFileName: 'final_video.webm'
        },
        audio: {
            language: 'en',
            outputDir: 'audio_blocks'
        }
    },
    scenes: [
        {
            id: 'intro',
            startTime: 0,
            audioText: "Welcome to the application demo. Let's explore the main features.",
            actions: async (page) => {
                await page.goto('http://localhost:3000/', { waitUntil: 'load' });
            },
            duration: 5
        },
        {
            id: 'closing',
            startTime: 5,
            audioText: "Thank you for watching this demonstration.",
            actions: async (page) => {
                await page.evaluate(() => window.scrollTo(0, 0));
            },
            duration: 5
        }
    ]
};
