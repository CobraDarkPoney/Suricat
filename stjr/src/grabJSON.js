const fs = require("fs");

module.exports = function grabJSON(path) {
    return JSON.parse(fs.readFileSync(path));
}