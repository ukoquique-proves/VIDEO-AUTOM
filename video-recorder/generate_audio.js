const gtts = require('gtts');
const fs = require('fs');
const path = require('path');

const blocks = [
    ["00", "Logistics operations are often buried in unstructured data—messy emails and invoices that cause critical delays. Today, we’re automating that chaos."],
    ["10", "This is the AI Logistics Hub. With one click, our pipeline uses Groq AI to extract structured entities from raw text. Watch as 5 complex scenarios are processed live."],
    ["20", "The system intelligently identifies company names, dates, and amounts, while also flagging status and urgency. Notice the color-coded badges providing instant operational visibility."],
    ["30", "Let's submit a custom critical event. A port blockage in Rotterdam. By submitting this raw text via our API, the AI instantly structures the data, identifying the $125,000 value at risk."],
    ["45", "The record appears instantly. Slack and email alerts have already been dispatched. This is professional-grade automation, built with Spring Boot 3 and a clean, layered architecture."],
    ["55", "Built for performance. Built for scale. This is the AI Logistics Automation Hub."]
];

const outputDir = path.join(__dirname, 'videos', 'audio_blocks');
if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
}

console.log(`Generating ${blocks.length} audio blocks...`);

async function generateAll() {
    for (const [i, text] of blocks) {
        const filename = path.join(outputDir, `block_${i}.mp3`);
        console.log(`Generating ${filename}...`);
        const speech = new gtts(text, 'en');
        
        await new Promise((resolve, reject) => {
            speech.save(filename, (err, result) => {
                if (err) {
                    console.error(`Error saving ${filename}:`, err);
                    reject(err);
                } else {
                    console.log(`Saved ${filename}`);
                    resolve(result);
                }
            });
        });
    }
    console.log("Done! Audio blocks generated in", outputDir);
}

generateAll().catch(console.error);
