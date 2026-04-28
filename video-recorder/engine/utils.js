const path = require('path');
const fs = require('fs');

/**
 * Parses the --scenario argument from CLI
 */
function getScenario() {
    const scenarioArg = process.argv.find(arg => arg.startsWith('--scenario='));
    if (!scenarioArg) {
        console.error('❌ ERROR: Missing --scenario argument.');
        console.error('Usage: node engine/script.js --scenario=scenarios/project/scenario.js');
        process.exit(1);
    }
    const scenarioRelativePath = scenarioArg.split('=')[1];
    // Resolve relative to the current working directory (usually the project root or video-recorder/)
    const absolutePath = path.resolve(process.cwd(), scenarioRelativePath);
    
    console.log(`🔍 Loading scenario from: ${absolutePath}`);
    if (!fs.existsSync(absolutePath)) {
        console.error(`❌ ERROR: Scenario file not found at ${absolutePath}`);
        process.exit(1);
    }
    
    const scenario = require(absolutePath);
    scenario.__baseDir = path.dirname(absolutePath);
    return scenario;
}

module.exports = { getScenario };
