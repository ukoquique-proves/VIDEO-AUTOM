const path = require('path');
const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

const videoFile = path.join(__dirname, 'videos', 'final_showcase.webm');
const audioDir = path.join(__dirname, 'videos', 'audio_blocks');
const outputFile = path.join(__dirname, 'videos', 'final_showcase_with_audio.webm');

const audioBlocks = [
    { file: 'block_00.mp3', start: 0 }, 
    { file: 'block_10.mp3', start: 10 },
    { file: 'block_20.mp3', start: 20 },
    { file: 'block_30.mp3', start: 30 },
    { file: 'block_45.mp3', start: 45 },
    { file: 'block_55.mp3', start: 55 }
];

async function getVideoDuration(filePath) {
    const { stdout } = await execPromise(
        `ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "${filePath}"`
    );
    return parseFloat(stdout.trim());
}

async function buildVideo() {
    // Probe the actual video duration so the last audio block's window is accurate.
    let videoDuration;
    try {
        videoDuration = await getVideoDuration(videoFile);
        console.log(`Video duration (ffprobe): ${videoDuration.toFixed(2)}s`);
    } catch (e) {
        console.error(`❌ Could not probe video duration: ${e.message}`);
        console.error(`   Make sure ffprobe is installed and "${videoFile}" exists.`);
        process.exit(1);
    }

    console.log("Validating audio block durations...");
    for (let i = 0; i < audioBlocks.length; i++) {
        const block = audioBlocks[i];
        // Use real video duration as the upper bound for the last block instead of a hardcoded guess.
        const nextStart = i + 1 < audioBlocks.length ? audioBlocks[i + 1].start : videoDuration;
        const maxDuration = nextStart - block.start;

        try {
            const filePath = path.join(audioDir, block.file);
            const { stdout } = await execPromise(`ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "${filePath}"`);
            const duration = parseFloat(stdout.trim());
            console.log(`- ${block.file}: ${duration.toFixed(2)}s (Max allowed: ${maxDuration.toFixed(2)}s)`);
            if (duration > maxDuration) {
                console.error(`\n❌ ERROR: ${block.file} is too long! It takes ${duration.toFixed(2)}s but only has ${maxDuration.toFixed(2)}s allocated before the next block. Please shorten the text in generate_audio.js.\n`);
                process.exit(1);
            }
        } catch (e) {
            console.warn(`⚠️ Could not validate duration for ${block.file}. Make sure ffprobe is installed. Proceeding...`);
        }
    }

    let filter = "";
    audioBlocks.forEach((block, index) => {
        const inputIdx = index + 1;
        // Removed atempo to keep natural voice speed
        filter += `[${inputIdx}:a]adelay=${block.start * 1000}|${block.start * 1000}[a${inputIdx}];`;
    });

    const mixInputs = audioBlocks.map((_, i) => `[a${i + 1}]`).join('');
    // amix=normalize=0 prevents volume spikes when inputs end
    // volume=1.2 provides a clear, natural level (diminished from 2.0)
    filter += `${mixInputs}amix=inputs=${audioBlocks.length}:duration=longest:normalize=0,volume=1.2[audio_out]`;

    const command = `ffmpeg -y -i "${videoFile}" ` + 
                    audioBlocks.map(b => `-i "${path.join(audioDir, b.file)}"`).join(' ') + 
                    ` -filter_complex "${filter}" -map 0:v -map "[audio_out]" -c:v copy -c:a libopus -shortest "${outputFile}"`;

    console.log("\nRunning FFmpeg command with natural pacing and stable volume...");

    try {
        await execPromise(command);
        console.log("Success! Final showcase created with natural voice and consistent volume.");
    } catch (error) {
         console.error(`Error executing ffmpeg: ${error.message}`);
    }
}

buildVideo();
