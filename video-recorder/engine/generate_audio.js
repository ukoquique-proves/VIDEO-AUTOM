const gtts = require('gtts');
const fs = require('fs');
const path = require('path');
const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);
const { getScenario } = require('./utils');

const scenario = getScenario();
const outputDir = path.join(scenario.__baseDir, 'videos', scenario.config.audio.outputDir);

if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
}

/**
 * Phase 1.4: Audio Length Safety
 * Checks if the generated audio duration is within the scene's duration.
 */
async function checkAudioDuration(filePath, sceneDuration, sceneId) {
    try {
        const { stdout } = await execPromise(`ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "${filePath}"`);
        const duration = parseFloat(stdout);
        if (duration > sceneDuration) {
            console.warn(`⚠️  WARNING: Audio for scene "${sceneId}" is longer than scene duration!`);
            console.warn(`   Audio: ${duration.toFixed(2)}s | Scene: ${sceneDuration.toFixed(2)}s`);
            console.warn(`   Consider increasing scene duration or shortening text.`);
        } else {
            console.log(`  - Duration check: ${duration.toFixed(2)}s / ${sceneDuration.toFixed(2)}s (OK)`);
        }
    } catch (error) {
        console.error(`  - Failed to check duration for ${sceneId}: ${error.message}`);
    }
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

        // Perform duration check
        await checkAudioDuration(filename, scene.duration, scene.id);
    }
    console.log("✅ Audio generation complete.");
}

generateAll().catch(console.error);
