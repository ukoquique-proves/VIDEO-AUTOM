const path = require('path');
const { exec } = require('child_process');
const util = require('util');
const fs = require('fs');
const execPromise = util.promisify(exec);
const { getScenario } = require('./utils');

const scenario = getScenario();
const videoDir = path.join(scenario.__baseDir, 'videos');
const videoFile = path.join(videoDir, scenario.config.video.outputFileName);
const audioDir = path.join(videoDir, scenario.config.audio.outputDir);
const outputFile = path.join(videoDir, scenario.config.video.finalOutputFileName);

async function buildVideo() {
    if (!fs.existsSync(videoFile)) {
        console.error(`❌ Video file not found: ${videoFile}`);
        process.exit(1);
    }

    const audioInputs = [];
    for (const scene of scenario.scenes) {
        if (!scene.audioText) continue;
        const filePath = path.join(audioDir, `block_${scene.id}.mp3`);
        if (fs.existsSync(filePath)) {
            audioInputs.push({ file: filePath, start: scene.startTime });
        }
    }

    let filter = "";
    audioInputs.forEach((input, index) => {
        const inputIdx = index + 1;
        filter += `[${inputIdx}:a]adelay=${input.start * 1000}|${input.start * 1000}[a${inputIdx}];`;
    });

    const mixInputs = audioInputs.map((_, i) => `[a${i + 1}]`).join('');
    filter += `${mixInputs}amix=inputs=${audioInputs.length}:duration=longest:normalize=0,volume=1.2[audio_out]`;

    const command = `ffmpeg -y -i "${videoFile}" ` + 
                    audioInputs.map(b => `-i "${b.file}"`).join(' ') + 
                    ` -filter_complex "${filter}" -map 0:v -map "[audio_out]" -c:v copy -c:a libopus -shortest "${outputFile}"`;

    console.log("🎬 Merging audio and video...");
    try {
        await execPromise(command);
        console.log(`✅ Success! Final video: ${outputFile}`);
    } catch (error) {
         console.error(`❌ FFmpeg failed: ${error.message}`);
    }
}

buildVideo();
