const path = require('path');
const { exec } = require('child_process');

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

let filter = "";
audioBlocks.forEach((block, index) => {
    const inputIdx = index + 1;
    // Removed atempo to keep natural voice speed
    filter += `[${inputIdx}:a]adelay=${block.start * 1000}|${block.start * 1000}[a${inputIdx}];`;
});

const mixInputs = audioBlocks.map((_, i) => `[a${i + 1}]`).join('');
// amix=normalize=0 prevents volume spikes when inputs end
// volume=2.0 provides a clear, professional level
filter += `${mixInputs}amix=inputs=${audioBlocks.length}:duration=longest:normalize=0,volume=2.0[audio_out]`;

const command = `ffmpeg -y -i "${videoFile}" ` + 
                audioBlocks.map(b => `-i "${path.join(audioDir, b.file)}"`).join(' ') + 
                ` -filter_complex "${filter}" -map 0:v -map "[audio_out]" -c:v copy -c:a libopus -shortest "${outputFile}"`;

console.log("Running FFmpeg command with natural pacing and stable volume...");

exec(command, (error, stdout, stderr) => {
    if (error) {
        console.error(`Error: ${error.message}`);
        return;
    }
    console.log("Success! Final showcase created with natural voice and consistent volume.");
});
