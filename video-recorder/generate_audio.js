const gtts = require('gtts');
const fs = require('fs');
const path = require('path');
const scenario = require('./scenario');

const outputDir = path.join(__dirname, 'videos', scenario.config.audio.outputDir);
if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
}

console.log(`Generating audio blocks for ${scenario.scenes.length} scenes...`);

async function generateAll() {
    for (const scene of scenario.scenes) {
        if (!scene.audioText) continue;
        
        const filename = path.join(outputDir, `block_${scene.id}.mp3`);
        console.log(`Generating ${filename}...`);
        const speech = new gtts(scene.audioText, scenario.config.audio.language);
        
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
    console.log("Done! Audio blocks generated from scenario.");
}

generateAll().catch(console.error);
