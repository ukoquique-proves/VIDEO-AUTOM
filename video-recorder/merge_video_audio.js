const path = require('path');
const { exec } = require('child_process');

const videoFile = path.join(__dirname, 'videos', 'final_showcase.webm');
const audioDir = path.join(__dirname, 'videos', 'audio_blocks');
const outputFile = path.join(__dirname, 'videos', 'final_showcase_with_audio.webm');

// We need to speed up the audio tracks to prevent overlap
// slot_duration is the time until the next block starts
const audioBlocks = [
    { file: 'block_00.mp3', start: 0,  duration: 11.47, slot: 10 }, 
    { file: 'block_10.mp3', start: 10, duration: 13.58, slot: 10 },
    { file: 'block_20.mp3', start: 20, duration: 14.30, slot: 10 },
    { file: 'block_30.mp3', start: 30, duration: 17.76, slot: 15 },
    { file: 'block_45.mp3', start: 45, duration: 12.74, slot: 10 },
    { file: 'block_55.mp3', start: 55, duration: 6.36,  slot: 7  }
];

let filter = "";
audioBlocks.forEach((block, index) => {
    const inputIdx = index + 1;
    // Calculate speed factor to fit in slot (plus 0.5s buffer)
    const targetDuration = block.slot - 0.5;
    const speed = Math.max(1.0, block.duration / targetDuration);
    
    // 1. Speed up audio using atempo
    // 2. Apply adelay
    filter += `[${inputIdx}:a]atempo=${speed.toFixed(2)},adelay=${block.start * 1000}|${block.start * 1000}[a${inputIdx}];`;
});

const mixInputs = audioBlocks.map((_, i) => `[a${i + 1}]`).join('');
filter += `${mixInputs}amix=inputs=${audioBlocks.length}:duration=longest,volume=2.5[audio_out]`;

const command = `ffmpeg -y -i "${videoFile}" ` + 
                audioBlocks.map(b => `-i "${path.join(audioDir, b.file)}"`).join(' ') + 
                ` -filter_complex "${filter}" -map 0:v -map "[audio_out]" -c:v copy -c:a libopus -shortest "${outputFile}"`;

console.log("Running FFmpeg command with overlap prevention (atempo)...");

exec(command, (error, stdout, stderr) => {
    if (error) {
        console.error(`Error: ${error.message}`);
        return;
    }
    console.log("Success! Video with audio created. Overlaps fixed by speeding up narration.");
});
