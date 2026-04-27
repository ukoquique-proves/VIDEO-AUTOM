const gtts = require('gtts');
const fs = require('fs');
const path = require('path');

// Shortened text blocks to fit naturally into the visual scenes without speeding up
const blocks = [
    ["00", "Logistics data is often trapped in messy emails and invoices. Today, we automate that chaos."],
    ["10", "This is the AI Logistics Hub. Watch as our Groq-powered pipeline extracts data from 5 scenarios live."],
    ["20", "The system identifies companies, dates, and amounts, using color-coded badges for instant operational visibility."],
    ["30", "Let's submit a critical event. A port blockage in Rotterdam. The AI instantly identifies the 125,000 dollar value at risk."],
    ["45", "The record appears instantly, and Slack alerts are dispatched. Professional automation built with Spring Boot 3."],
    ["55", "Built for scale. AI Logistics Hub."]
];

const outputDir = path.join(__dirname, 'videos', 'audio_blocks');
if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
}

console.log(`Generating ${blocks.length} shortened audio blocks for natural pacing...`);

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
    console.log("Done! Shortened audio blocks generated.");
}

generateAll().catch(console.error);
