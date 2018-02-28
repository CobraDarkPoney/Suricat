const fs = require('fs');
const grabJSON = require('./grabJSON');
const readlineSync = require('readline-sync');

action = process.argv[2];
jsonFilePath = process.argv[3];

// Actions
if (action == null) {
    console.log('[STJR: Error] No STJR action specified.');
    return;
}
if (action != 'generate' && action != 'randomize') {
    console.log('[STJR: Error] Invalid STJR action specified.');
    return;
}

// JSON File Path
if (jsonFilePath == undefined) {
    console.log('[STJR: Error] No file path argument.');
    return;
}
if (!jsonFilePath.match(/^.+\.json$/)) {
    console.log('[STJR: Error] The file specified is not a JSON file.');
    return;
}
if (action == 'randomize' && !fs.existsSync('./'+jsonFilePath)) {
    console.log('[STJR: Error] File specified by path does not exist.');
    return;
}


if (action == 'randomize') {

    json = grabJSON('./'+jsonFilePath);
    transactions = [...json.transactions];

    transactions.sort(() => Math.random()*2-1);
    transactions = transactions.map((e, index) => Object.assign({}, e, {index}));

    const {models} = json;
    newJson = { models, transactions };

    fs.writeFileSync('./'+jsonFilePath.replace('\.json', '')+'.stjrRandomized.json', JSON.stringify(newJson, null, 4));

}
/*
if (action == 'generate') {

    var amount = '';
    while (!amount.match(/^\d$/)) {
        amount = readlineSync.question('Please precise the amount of transactions you would like to generate: ');
        if (!amount.match(/^\d$/))
            console.log('[STJR: Error] Please precise a valid amount.');
    }

    amount = parseInt(amount, 10);
    json = [];

    lockpool = ['', '', '']

    for (i = 0; i < amount; i++) {
        json.push()
    }

}
*/