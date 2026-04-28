const gtts = require('gtts');
const fs = require('fs');
const path = require('path');
const { getScenario } = require('./utils');

const scenario = getScenario();
const outputDir = path.join(scenario.__baseDir, 'videos', scenario.config.audio.outputDir);

if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
}

async function generateAll() {
    console.log(`🎙️ Generating audio for ${scenario.scenes.length} scenes...`);
    for (const scene of scenario.scenes) {
        if (!scene.audioText) continue;
        
        const filename = path.join(outputDir, `block_${scene.id}.mp3`);
        const speech = new gtts(scene.audioText, scenario.config.audio.language);
        
        await new Promise((resolve, reject) => {
            speech.save(filename, (err) => {
                if (err) reject(err);
                else {
                    console.log(`  - Saved: ${path.basename(filename)}`);
                    resolve();
                }
            });
        });
    }
    console.log("✅ Audio generation complete.");
}

generateAll().catch(console.error);
