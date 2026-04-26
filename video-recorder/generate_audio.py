import sys
from gtts import gTTS
import os

# Text blocks from NARRATION_SCRIPT.md
blocks = [
    ("00", "Logistics operations are often buried in unstructured data—messy emails and invoices that cause critical delays. Today, we’re automating that chaos."),
    ("10", "This is the AI Logistics Hub. With one click, our pipeline uses Groq AI to extract structured entities from raw text. Watch as 5 complex scenarios are processed live."),
    ("20", "The system intelligently identifies company names, dates, and amounts, while also flagging status and urgency. Notice the color-coded badges providing instant operational visibility."),
    ("30", "Let's submit a custom critical event. A port blockage in Rotterdam. By submitting this raw text via our API, the AI instantly structures the data, identifying the $125,000 value at risk."),
    ("45", "The record appears instantly. Slack and email alerts have already been dispatched. This is professional-grade automation, built with Spring Boot 3 and a clean, layered architecture."),
    ("55", "Built for performance. Built for scale. This is the AI Logistics Automation Hub.")
]

output_dir = "video-recorder/videos/audio_blocks"
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

print(f"Generating {len(blocks)} audio blocks...")

for i, text in blocks:
    filename = f"{output_dir}/block_{i}.mp3"
    print(f"Generating {filename}...")
    tts = gTTS(text=text, lang='en', tld='com')
    tts.save(filename)

print("Done! Audio blocks generated in", output_dir)
