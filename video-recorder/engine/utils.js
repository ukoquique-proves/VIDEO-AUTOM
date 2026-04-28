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
    
    if (!scenarioRelativePath) {
        console.error('❌ ERROR: --scenario argument provided but no path specified.');
        console.error('Usage: node engine/script.js --scenario=scenarios/project/scenario.js');
        process.exit(1);
    }

    // Resolve relative to the current working directory (usually the project root or video-recorder/)
    const absolutePath = path.resolve(process.cwd(), scenarioRelativePath);
    
    if (!fs.existsSync(absolutePath)) {
        console.error(`❌ ERROR: Scenario file not found at: ${absolutePath}`);
        console.error(`   Check if the path "scenarios/${scenarioRelativePath}" is correct relative to video-recorder/`);
        process.exit(1);
    }

    console.log(`🔍 Loading scenario from: ${absolutePath}`);
    const scenario = require(absolutePath);
    scenario.__baseDir = path.dirname(absolutePath);
    return scenario;
}

module.exports = { getScenario };
