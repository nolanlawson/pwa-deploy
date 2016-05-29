#!/usr/bin/env node

var yargs = require('yargs')
  .usage('Usage: $0 <path> [options]')
  .example('$0 /path/to/www/', 'Load webapp on first available device')
  .example('$0 /path/to/www/ -s <deviceid>', 'Load webapp on specified device')
  .example('$0 devices', 'List all available devices/emulators')
  .command('devices', 'list available devices')
  .describe('d', 'specific device id. do `pwa-deploy devices` to see available')
  .alias('d', 'device')
  .help('h')
  .alias('h', 'help');
var Promise = require('bluebird');
if (!global.Promise) {
  global.Promise = Promise; // required for denodeify
}
var childProcess = require('child-process-promise');
var adb = require('./adb');

var arg = yargs.argv;

if (arg._[0] === 'devices') {
  Promise.resolve().then(function () {
    return adb().then(function (adbPath) {
      return childProcess.exec(adbPath + ' devices')
    });
  }).then(function (res) {
    process.stdout.write(res.stdout);
    process.exit(0);
  }).catch(function (err) {
    console.error(err.stack);
    process.exit(1);
  });
} else if (!arg._.length) {
  yargs.showHelp();
  process.exit(0);
} else {

}