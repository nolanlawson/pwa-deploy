#!/usr/bin/env node

'use strict';

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
var denodeify = require('denodeify');
var childProcess = require('child-process-promise');
var path = require('path');
var adb = require('./adb');
var arg = yargs.argv;
var launchApk = require('./launchApk');

if (!arg._.length) {
  yargs.showHelp();
  process.exit(0);
} else if (arg._[0] === 'devices') {
  Promise.resolve().then(function () {
    return adb().then(function (adbPath) {
      return childProcess.exec(adbPath + ' devices');
    });
  }).then(function (res) {
    process.stdout.write(res.stdout);
    process.exit(0);
  }).catch(function (err) {
    console.error(err.stack);
    process.exit(1);
  });
} else { // normal operation
  var dir = path.resolve(arg._[0]);
  return launchApk(dir).catch(function (err) {
    console.error(err.stack);
    process.exit(1);
  });
}
