const path = require('path');
const { exec } = require('child_process');
const util = require('util');
const fs = require('fs');
const execPromise = util.promisify(exec);
const scenario = require('./scenario');

const videoFile = path.join(__dirname, 'videos', scenario.config.video.outputFileName);
const audioDir = path.join(__dirname, 'videos', scenario.config.audio.outputDir);
const outputFile = path.join(__dirname, 'videos', scenario.config.video.finalOutputFileName);

async function getVideoDuration(filePath) {
    const { stdout } = await execPromise(
        `ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "${filePath}"`
    );
    return parseFloat(stdout.trim());
}

async function buildVideo() {
    let videoDuration;
    try {
        videoDuration = await getVideoDuration(videoFile);
        console.log(`Video duration (ffprobe): ${videoDuration.toFixed(2)}s`);
    } catch (e) {
        console.error(`❌ Could not probe video duration: ${e.message}`);
        process.exit(1);
    }

    console.log("Validating audio block durations against scenario...");
    const audioInputs = [];
    
    for (const scene of scenario.scenes) {
        if (!scene.audioText) continue;
        
        const audioFile = `block_${scene.id}.mp3`;
        const filePath = path.join(audioDir, audioFile);
        
        if (!fs.existsSync(filePath)) {
            console.warn(`⚠️ Audio file ${audioFile} missing, skipping...`);
            continue;
        }

        try {
            const { stdout } = await execPromise(`ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "${filePath}"`);
            const duration = parseFloat(stdout.trim());
            console.log(`- ${audioFile}: ${duration.toFixed(2)}s (Available in scene: ${scene.duration.toFixed(2)}s)`);
            
            if (duration > scene.duration) {
                console.error(`\n❌ ERROR: ${audioFile} is too long! (${duration.toFixed(2)}s > ${scene.duration.toFixed(2)}s)\n`);
                process.exit(1);
            }
            
            audioInputs.push({
                file: filePath,
                start: scene.startTime
            });
        } catch (e) {
            console.warn(`⚠️ Could not validate duration for ${audioFile}.`);
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

    console.log("\nMerging audio and video...");

    try {
        await execPromise(command);
        console.log(`Success! Final video created: ${outputFile}`);
    } catch (error) {
         console.error(`Error executing ffmpeg: ${error.message}`);
    }
}

buildVideo();
